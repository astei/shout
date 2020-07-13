package me.steinborn.shout.platform;

import org.checkerframework.common.value.qual.IntRange;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSchedulerTaskBuilder implements ShoutScheduler.TaskBuilder {
    protected final Runnable task;
    protected long delayInMillis = 0;
    protected long repeatInMillis = 0;

    protected AbstractSchedulerTaskBuilder(Runnable task) {
        this.task = Objects.requireNonNull(task, "task");
    }

    @Override
    public ShoutScheduler.TaskBuilder delay(@IntRange(from = 0) long time, TimeUnit unit) {
        if (time < 0) {
            throw new IllegalArgumentException("Time argument must be positive");
        }
        this.delayInMillis = unit.toMillis(time);
        return this;
    }

    @Override
    public ShoutScheduler.TaskBuilder repeat(@IntRange(from = 0) long time, TimeUnit unit) {
        if (time < 0) {
            throw new IllegalArgumentException("Time argument must be positive");
        }
        this.repeatInMillis = unit.toMillis(time);
        return this;
    }
}
