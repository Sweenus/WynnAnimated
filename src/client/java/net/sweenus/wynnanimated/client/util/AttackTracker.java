package net.sweenus.wynnanimated.client.util;

public class AttackTracker {
    public static boolean attackInitiated = false;
    public static int lastAnimationTick = Integer.MIN_VALUE;
    public static final int SWING_SUPPRESS_TICKS = 60;
}
