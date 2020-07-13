package me.steinborn.shout.platform;

import org.checkerframework.common.value.qual.IntRange;

import java.util.concurrent.TimeUnit;

/**
 * Provides a scheduler abstraction. The abstraction is modeled loosely on the Velocity API.
 */
public interface ShoutScheduler {
    /**
     * Creates a confine that runs the task on the main server thread. In the event a "main server thread" does not
     * exist, it is permitted for this function to forward to {@link #async()} instead.
     *
     * @return a confine that will schedule tasks on the main server thread
     */
    SchedulerConfine server();

    /**
     * Creates a confine that runs the task on a different thread pool, one that is guaranteed to never execute tasks on
     * the "main server thread".
     *
     * @return a confine that will schedule tasks asynchronously
     */
    SchedulerConfine async();

    /**
     * A factory to create {@link TaskBuilder}s, called a "confine" because the thread the task will be scheduled changes
     * the behavior of the scheduler.
     */
    interface SchedulerConfine {
        TaskBuilder createTaskBuilder(Runnable runnable);
    }

    interface TaskBuilder {
        /**
         * Specifies that the task should delay its execution by the specified amount of time.
         *
         * @param time the time to delay by
         * @param unit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        TaskBuilder delay(@IntRange(from = 0) long time, TimeUnit unit);

        /**
         * Specifies that the task should continue running after waiting for the specified amount, until
         * it is cancelled.
         *
         * @param time the time to delay by
         * @param unit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        TaskBuilder repeat(@IntRange(from = 0) long time, TimeUnit unit);

        /**
         * Schedules this task for execution.
         *
         * @return the scheduled task
         */
        Task schedule();
    }

    interface Task {
        void cancel();
    }
}
