package net.sweenus.wynnanimated.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.wynnanimated.client.AnimationRegistry;
import net.sweenus.wynnanimated.client.WynnanimatedClient;
import net.sweenus.wynnanimated.client.config.ModConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Asynchronously fetches and caches player class types from the Wynncraft API.
 * Uses player usernames (not entity UUIDs, which are offline-mode on Wynncraft).
 * Flow: GET /v3/player/{username} -> activeCharacter UUID -> GET /v3/player/{username}/characters/{charUuid} -> type
 */
public final class WynnPlayerClassCache {

    private static final String API_BASE = "https://api.wynncraft.com/v3/player/";
    private static final long RETRY_DELAY_MS = 60_000;
    private static final long RATE_LIMIT_WINDOW_MS = 60_000; // 1 minute
    private static int MAX_REQUESTS_PER_WINDOW = 60;

    private static final ConcurrentHashMap<String, String> classCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> failedTimestamps = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> cachedTimestamps = new ConcurrentHashMap<>(); // Track cache timestamps
    private static final Set<String> pendingRequests = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Rate limiting: track request timestamps within the current window
    private static final Queue<Long> requestTimestamps = new ArrayDeque<>();
    private static final ReentrantLock rateLimitLock = new ReentrantLock();
    private static long windowStartTime = 0;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static double getAnimationRangeSq() {
        int range = ModConfig.get().animationRange;
        return (double) range * range;
    }

    // Schedule cache refresh task every minute
    static {
        java.util.Timer timer = new java.util.Timer("WynnPlayerClassCache-Refresh", true);
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                Set<String> usernamesToCheck = new HashSet<>();

                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && client.world != null) {
                    double animationRangeSq = getAnimationRangeSq();

                    // Include local player in cache
                    String localUsername = getUsernameFromPlayer(client.player);
                    if (localUsername != null) {
                        Long lastRefreshed = cachedTimestamps.get(localUsername);
                        if (lastRefreshed != null && now - lastRefreshed >= 5 * 60_000) {
                            fetchPlayerClassAsync(localUsername);
                            if (AnimationRegistry.debugMode)
                                System.out.println(WynnanimatedClient.LOG_ID + " Refreshing class data for local player " + localUsername);
                        }
                    }

                    // Check nearby players
                    for (PlayerEntity player : client.world.getPlayers()) {
                        String username = getUsernameFromPlayer(player);
                        if (username == null || player == client.player) continue;

                        double distanceSq = player.squaredDistanceTo(client.player);
                        if (distanceSq <= animationRangeSq) { // Player is within animation range
                            usernamesToCheck.add(username);

                            Long lastRefreshed = cachedTimestamps.get(username);
                            if (lastRefreshed != null && now - lastRefreshed >= 5 * 60_000) { // 5 minutes
                                fetchPlayerClassAsync(username); // Refresh this player's data
                                if (AnimationRegistry.debugMode)
                                    System.out.println(WynnanimatedClient.LOG_ID + " Refreshing class data for " + username);
                            }
                        }
                    }
                }
            }
        }, 10_000, 60_000); // Start after 10 seconds, run every minute to check for expired entries
    }

    public static void applyConfig() {
        ModConfig cfg = ModConfig.get();
        MAX_REQUESTS_PER_WINDOW = cfg.rateLimit;
    }

    /**
     * Returns the cached class type for a player, or null if not yet fetched.
     * Automatically triggers an async API fetch on the first call for each player.
     * API class types: ARCHER, WARRIOR, MAGE, ASSASSIN, SHAMAN
     *
     * @param username the player's in-game username
     */
    public static String getPlayerClass(String username) {
        String cached = classCache.get(username);
        if (cached != null) return cached;

        // Don't retry too soon after a failure
        Long failedAt = failedTimestamps.get(username);
        if (failedAt != null && System.currentTimeMillis() - failedAt < RETRY_DELAY_MS) return null;

        // Check rate limit before proceeding
        if (!isBelowRateLimit()) {
            if (AnimationRegistry.debugMode) {
                System.out.println(WynnanimatedClient.LOG_ID + " Rate limit exceeded for " + username + ", skipping request");
            }
            return null;
        }

        // Trigger async fetch if not already in flight
        if (pendingRequests.add(username)) {
            fetchPlayerClassAsync(username);
        }

        return null;
    }

    private static boolean isBelowRateLimit() {
        rateLimitLock.lock();
        try {
            long now = System.currentTimeMillis();

            // Reset window if needed
            if (now - windowStartTime >= RATE_LIMIT_WINDOW_MS) {
                requestTimestamps.clear();
                windowStartTime = now;
                if (AnimationRegistry.debugMode)
                    System.out.println(WynnanimatedClient.LOG_ID + " Resetting player cache rate limit");
            }

            // Check if we're under the limit
            if (requestTimestamps.size() < MAX_REQUESTS_PER_WINDOW) {
                requestTimestamps.add(now);
                if (AnimationRegistry.debugMode)
                    System.out.println(WynnanimatedClient.LOG_ID + " Player cache rate limit = " + requestTimestamps.size() + "/" + MAX_REQUESTS_PER_WINDOW);
                return true;
            }

            // If we're at the limit, check if the oldest request is still within the window
            Long oldest = requestTimestamps.peek();
            if (oldest != null && now - oldest < RATE_LIMIT_WINDOW_MS) {
                if (AnimationRegistry.debugMode)
                    System.out.println(WynnanimatedClient.LOG_ID + " Rate limit age: " + ((now - windowStartTime) / 1000) + "/" + (RATE_LIMIT_WINDOW_MS / 1000));
                return false; // Rate limit exceeded
            }

            // If oldest request is outside the window, remove it and allow new request
            requestTimestamps.poll();
            requestTimestamps.add(now);
            return true;
        } finally {
            rateLimitLock.unlock();
        }
    }

    private static void fetchPlayerClassAsync(String username) {
        String playerUrl = API_BASE + username;
        HttpRequest playerRequest = HttpRequest.newBuilder()
                .uri(URI.create(playerUrl))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        httpClient.sendAsync(playerRequest, HttpResponse.BodyHandlers.ofString())
                .thenCompose(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("Player API returned " + response.statusCode());
                    }

                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    if (!json.has("activeCharacter") || json.get("activeCharacter").isJsonNull()) {
                        throw new RuntimeException("No active character");
                    }
                    String activeCharId = json.get("activeCharacter").getAsString();

                    // Fetch the active character's class type
                    String charUrl = playerUrl + "/characters/" + activeCharId;
                    HttpRequest charRequest = HttpRequest.newBuilder()
                            .uri(URI.create(charUrl))
                            .timeout(Duration.ofSeconds(10))
                            .GET()
                            .build();

                    return httpClient.sendAsync(charRequest, HttpResponse.BodyHandlers.ofString());
                })
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                        if (json.has("type") && !json.get("type").isJsonNull()) {
                            String type = json.get("type").getAsString();
                            classCache.put(username, type);
                            cachedTimestamps.put(username, System.currentTimeMillis()); // Update timestamp on successful fetch
                            failedTimestamps.remove(username);
                            if (AnimationRegistry.debugMode) {
                                System.out.println(WynnanimatedClient.LOG_ID + " Fetched class for " + username + ": " + type);
                            }
                        }
                    }

                    // Clean up pending requests and rate limit tracking
                    pendingRequests.remove(username);
                    rateLimitLock.lock();
                    try {
                        requestTimestamps.removeIf(timestamp -> timestamp == System.currentTimeMillis());
                    } finally {
                        rateLimitLock.unlock();
                    }
                })
                .exceptionally(e -> {
                    failedTimestamps.put(username, System.currentTimeMillis());
                    pendingRequests.remove(username);
                    if (AnimationRegistry.debugMode) {
                        System.out.println(WynnanimatedClient.LOG_ID + " Failed to fetch class for " + username + ": " + e.getMessage());
                    }
                    return null;
                });
    }

    /**
     * Extracts the Minecraft username from a PlayerEntity.
     */
    private static String getUsernameFromPlayer(PlayerEntity player) {
        if (player == null || player.getGameProfile() == null || player.getGameProfile().name() == null) {
            return null;
        }
        return player.getGameProfile().name();
    }
}