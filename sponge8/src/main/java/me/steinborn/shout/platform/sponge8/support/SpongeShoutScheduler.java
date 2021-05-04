package me.steinborn.shout.platform.sponge8.support;

import com.google.inject.Inject;
import java.util.concurrent.TimeUnit;
import me.steinborn.shout.platform.AbstractSchedulerTaskBuilder;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.sponge8.ShoutSpongeBootstrap;
import org.spongepowered.api.Engine;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Scheduler;

public class SpongeShoutScheduler implements ShoutScheduler {
    private final SchedulerConfine serverConfine;
    private final SchedulerConfine asyncConfine;

    @Inject
    public SpongeShoutScheduler(ShoutSpongeBootstrap plugin, Engine engine, Game game) {
        this.serverConfine = new SpongeSchedulerConfine(plugin, engine.scheduler());
        this.asyncConfine = new SpongeSchedulerConfine(plugin, game.asyncScheduler());
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
        private final ShoutSpongeBootstrap plugin;
        private final Scheduler scheduler;

        private SpongeSchedulerConfine(ShoutSpongeBootstrap plugin, Scheduler scheduler) {
            this.plugin = plugin;
            this.scheduler = scheduler;
        }

        @Override
        public TaskBuilder createTaskBuilder(Runnable runnable) {
            return new SpongeTaskBuilder(runnable, this.plugin, this.scheduler);
        }
    }

    private static class SpongeTaskBuilder extends AbstractSchedulerTaskBuilder {
        private final ShoutSpongeBootstrap plugin;
        private final Scheduler scheduler;

        private SpongeTaskBuilder(Runnable task, ShoutSpongeBootstrap plugin, Scheduler scheduler) {
            super(task);
            this.plugin = plugin;
            this.scheduler = scheduler;
        }

        private ScheduledTask platformSchedule() {
            org.spongepowered.api.scheduler.Task.Builder builder = org.spongepowered.api.scheduler.Task.builder()
                    .execute(this.task)
                    .plugin(this.plugin.getContainer())
                    .delay(this.delayInMillis, TimeUnit.MILLISECONDS);
            if (this.repeatInMillis != 0) {
                builder.interval(this.repeatInMillis, TimeUnit.MILLISECONDS);
            }

            return scheduler.submit(builder.build());
        }

        @Override
        public Task schedule() {
            return this.platformSchedule()::cancel;
        }
    }
}
