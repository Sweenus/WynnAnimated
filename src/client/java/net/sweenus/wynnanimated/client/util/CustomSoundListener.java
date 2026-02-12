package net.sweenus.wynnanimated.client.util;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomSoundListener implements SoundInstanceListener {

    private static final double POSITION_TOLERANCE = 2.0;

    // Map to store active sounds and their start time
    private final Map<SoundPositionKey, SoundInfo> activeSounds = new ConcurrentHashMap<>();

    @Override
    public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet, float range) {
        Identifier soundId = sound.getId();
        double x = sound.getX();
        double y = sound.getY();
        double z = sound.getZ();

        float duration = getExpectedSoundDuration(sound);
        long startTime = System.currentTimeMillis();

        SoundPositionKey key = new SoundPositionKey(soundId, x, y, z);
        activeSounds.put(key, new SoundInfo(startTime, duration));
    }

    private float getExpectedSoundDuration(SoundInstance sound) {
        return 200;
    }

    public boolean isSpecificSoundPlayingAtCoordinates(Identifier soundId, double x, double y, double z) {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<SoundPositionKey, SoundInfo>> iterator = activeSounds.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<SoundPositionKey, SoundInfo> entry = iterator.next();

            // Check if sound has exceeded its duration
            if ((currentTime - entry.getValue().startTime) > entry.getValue().duration) {
                iterator.remove();
            } else {
                // Check if this is the matching sound position
                SoundPositionKey key = entry.getKey();
                if (key.soundId.equals(soundId) &&
                        Math.abs(key.x - x) <= POSITION_TOLERANCE &&
                        Math.abs(key.y - y) <= POSITION_TOLERANCE &&
                        Math.abs(key.z - z) <= POSITION_TOLERANCE) {
                    return true;
                }
            }
        }

        return false;
    }

    private static class SoundPositionKey {
        Identifier soundId;
        double x, y, z;

        public SoundPositionKey(Identifier soundId, double x, double y, double z) {
            this.soundId = soundId;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SoundPositionKey k)) return false;
            return Double.compare(k.x, x) == 0 && Double.compare(k.y, y) == 0
                    && Double.compare(k.z, z) == 0 && soundId.equals(k.soundId);
        }

        @Override
        public int hashCode() {
            int result = soundId.hashCode();
            result = 31 * result + Double.hashCode(x);
            result = 31 * result + Double.hashCode(y);
            result = 31 * result + Double.hashCode(z);
            return result;
        }
    }

    private static class SoundInfo {
        long startTime;
        float duration;

        public SoundInfo(long startTime, float duration) {
            this.startTime = startTime;
            this.duration = duration;
        }
    }
}