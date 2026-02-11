package net.sweenus.wynnanimated.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static ModConfig INSTANCE = new ModConfig();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // General
    public boolean debugMode = false;
    public boolean showArms = true;
    public int animationRange = 20;
    public int stanceTimeoutTicks = 40;

    // Archer spell speeds
    public float arrowStormSpeed = 2.5f;
    public float escapeSpeed = 1.5f;
    public float bombSpeed = 2.0f;
    public float arrowShieldSpeed = 1.6f;

    // Assassin spell speeds
    public float spinAttackSpeed = 2.5f;
    public float dashSpeed = 1.8f;
    public float multiHitSpeed = 2.1f;
    public float smokeBombSpeed = 1.9f;

    // Warrior spell speeds
    public float bashSpeed = 2.1f;
    public float chargeSpeed = 1.4f;
    public float warScreamSpeed = 2.3f;
    public float uppercutSpeed = 1.8f;

    // Mage spell speeds
    public float healSpeed = 1.5f;
    public float teleportSpeed = 2.0f;
    public float meteorSpeed = 1.8f;
    public float iceSnakeSpeed = 1.9f;

    // Shaman spell speeds
    public float totemSpeed = 2.5f;
    public float haulSpeed = 1.8f;
    public float uprootSpeed = 2.0f;
    public float auraSpeed = 1.0f;

    // Stance speed
    public float bowStanceReadySpeed = 1.0f;

    public static ModConfig get() {
        return INSTANCE;
    }

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("wynnanimated.json");
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                INSTANCE = GSON.fromJson(json, ModConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ModConfig();
                }
            } catch (IOException e) {
                System.err.println("[WynnAnimated] Failed to load config: " + e.getMessage());
                INSTANCE = new ModConfig();
            }
        } else {
            INSTANCE = new ModConfig();
            save();
        }
    }

    public static void save() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("wynnanimated.json");
        try {
            Files.writeString(configPath, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            System.err.println("[WynnAnimated] Failed to save config: " + e.getMessage());
        }
    }
}
