package me.steinborn.shout.platform.sponge.support;

import com.google.inject.Inject;
import me.steinborn.shout.platform.AbstractSchedulerTaskBuilder;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.sponge.ShoutSpongeBootstrap;
import org.spongepowered.api.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;

public class SpongeShoutScheduler implements ShoutScheduler {
    private final SchedulerConfine serverConfine;
    private final SchedulerConfine asyncConfine;

    @Inject
    public SpongeShoutScheduler(ShoutSpongeBootstrap plugin, Scheduler scheduler) {
        this.serverConfine = new SpongeSchedulerConfine(false, plugin, scheduler);
        this.asyncConfine = new SpongeSchedulerConfine(true, plugin, scheduler);
    }

    @Override
    public SchedulerConfine server() {
        return this.serverConfine;
    }

    @Override
    public SchedulerConfine async() {
        return this.asyncConfine;
    }

    private static class SpongeSchedulerConfine implements SchedulerConfine {
        private final boolean async;
        private final ShoutSpongeBootstrap plugin;
        private final Scheduler scheduler;

        private SpongeSchedulerConfine(boolean async, ShoutSpongeBootstrap plugin, Scheduler scheduler) {
            this.async = async;
            this.plugin = plugin;
            this.scheduler = scheduler;
        }

        @Override
        public TaskBuilder createTaskBuilder(Runnable runnable) {
            return new SpongeTaskBuilder(runnable, this.async, this.plugin, this.scheduler);
        }
    }

    private static class SpongeTaskBuilder extends AbstractSchedulerTaskBuilder {
        private final boolean async;
        private final ShoutSpongeBootstrap plugin;
        private final Scheduler scheduler;

        private SpongeTaskBuilder(Runnable task, boolean async, ShoutSpongeBootstrap plugin, Scheduler scheduler) {
            super(task);
            this.async = async;
            this.plugin = plugin;
            this.scheduler = scheduler;
        }

        private org.spongepowered.api.scheduler.Task platformSchedule() {
            org.spongepowered.api.scheduler.Task.Builder builder = scheduler.createTaskBuilder()
                    .execute(this.task)
                    .delay(this.delayInMillis, TimeUnit.MILLISECONDS);
            if (this.async) {
                builder.async();
            }
            if (this.repeatInMillis != 0) {
                builder.interval(this.repeatInMillis, TimeUnit.MILLISECONDS);
            }
            return builder.submit(this.plugin);
        }

        @Override
        public Task schedule() {
            return this.platformSchedule()::cancel;
        }
    }
}
