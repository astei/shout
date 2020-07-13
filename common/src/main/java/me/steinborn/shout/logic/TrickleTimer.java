package me.steinborn.shout.logic;

import java.util.Objects;

/**
 * A timer that "trickles" down an invocation when it has been called a specified number of times.
 */
public class TrickleTimer implements Runnable {
    private int timesRun;
    private int cap;
    private final Runnable task;

    public TrickleTimer(int cap, Runnable runnable) {
        if (cap < 0) {
            throw new IllegalArgumentException("cap must be positive or zero");
        }
        this.cap = cap;
        this.task = Objects.requireNonNull(runnable, "runnable");
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    @Override
    public void run() {
        if (++timesRun >= cap) {
            timesRun = 0;
            task.run();
        }
    }
}
