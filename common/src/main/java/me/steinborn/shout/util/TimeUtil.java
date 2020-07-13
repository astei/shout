package me.steinborn.shout.util;

public final class TimeUtil {
    private static final long MILLISECONDS_IN_A_TICK = 50;

    private TimeUtil() {
        throw new AssertionError("Invalid construction");
    }

    public static long toTicks(long millis) {
        return millis / MILLISECONDS_IN_A_TICK;
    }
}
