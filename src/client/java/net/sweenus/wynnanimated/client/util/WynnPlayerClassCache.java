package net.sweenus.wynnanimated.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.sweenus.wynnanimated.client.WynnanimatedClient;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;
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
    private static final int MAX_REQUESTS_PER_WINDOW = 60;

    private static final ConcurrentHashMap<String, String> classCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> failedTimestamps = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<String, Boolean> pendingRequests = ConcurrentHashMap.newKeySet();

    // Rate limiting: track request timestamps within the current window
    private static final Queue<Long> requestTimestamps = new ArrayDeque<>();
    private static final ReentrantLock rateLimitLock = new ReentrantLock();
    private static long windowStartTime = 0;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * Returns the cached class type for a player, or null if not yet fetched.
     * Automatically triggers an async API fetch on the first call for each player.
     * API class types: ARCHER, WARRIOR, MAGE, ASSASSIN, SHAMAN
     *
     * @param username the player's in-game username
     */
    public static @Nullable String getPlayerClass(String username) {
        String cached = classCache.get(username);
        if (cached != null) return cached;

        // Don't retry too soon after a failure
        Long failedAt = failedTimestamps.get(username);
        if (failedAt != null && System.currentTimeMillis() - failedAt < RETRY_DELAY_MS) return null;

        // Check rate limit before proceeding
        if (!isBelowRateLimit()) {
            if (WynnanimatedClient.debugMode) {
                System.out.println("[WynnAnimated] Rate limit exceeded for " + username + ", skipping request");
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
            }
            
            // Check if we're under the limit
            if (requestTimestamps.size() < MAX_REQUESTS_PER_WINDOW) {
                requestTimestamps.add(now);
                return true;
            }
            
            // If we're at the limit, check if the oldest request is still within the window
            Long oldest = requestTimestamps.peek();
            if (oldest != null && now - oldest < RATE_LIMIT_WINDOW_MS) {
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
                            failedTimestamps.remove(username);
                            if (WynnanimatedClient.debugMode) {
                                System.out.println("[WynnAnimated] Fetched class for " + username + ": " + type);
                            }
                        }
                    }
                    pendingRequests.remove(username);
                    // Remove timestamp when request is complete to maintain rate limit accuracy
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
                    if (WynnanimatedClient.debugMode) {
                        System.out.println("[WynnAnimated] Failed to fetch class for " + username + ": " + e.getMessage());
                    }
                    return null;
                });
    }
}
