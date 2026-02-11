package net.sweenus.wynnanimated.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.sweenus.wynnanimated.client.config.ModConfig;
import net.sweenus.wynnanimated.client.util.WynnCooldownObserver;
import net.sweenus.wynnanimated.client.util.WynnPlayerClassCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WynnanimatedClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("WynnAnimated");
    public static final String MOD_ID = "wynnanimated";
    public static final String LOG_ID = "[WynnAnimated]";
    public static final String WYNNTILS_MOD_ID = "wynntils";

    @Override
    public void onInitializeClient() {
        ModConfig.load();
        AnimationRegistry.registerAnimations();
        AnimationRegistry.applyConfig();
        AnimationRegistry.createLists();
        WynnPlayerClassCache.applyConfig();

        // Track cooldowns for basic attack animation speed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player instanceof AbstractClientPlayerEntity player) {
                WynnCooldownObserver.tick(player);
            }
        });

        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            if (client.getSoundManager() != null) {
                client.getSoundManager().registerListener(AnimationRegistry.soundListener);
                if (AnimationRegistry.debugMode)
                    System.out.println(WynnanimatedClient.LOG_ID + " Registered WynnAnimated Sound Listener");
            }
        });

    }

}
