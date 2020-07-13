package me.steinborn.shout.platform.velocity.support;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import me.steinborn.shout.platform.AbstractSchedulerTaskBuilder;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.velocity.ShoutVelocityBootstrap;

import java.util.concurrent.TimeUnit;

public class VelocityShoutScheduler implements ShoutScheduler, ShoutScheduler.SchedulerConfine {
    private final ProxyServer server;
    private final ShoutVelocityBootstrap plugin;

    @Inject
    public VelocityShoutScheduler(ProxyServer server, ShoutVelocityBootstrap plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public SchedulerConfine server() {
        return this;
    }

    @Override
    public SchedulerConfine async() {
        return this;
    }

    @Override
    public TaskBuilder createTaskBuilder(Runnable runnable) {
        return new VelocityTaskBuilder(server, plugin, runnable);
    }

    private static class VelocityTaskBuilder extends AbstractSchedulerTaskBuilder {
        private final ProxyServer server;
        private final ShoutVelocityBootstrap plugin;

        public VelocityTaskBuilder(ProxyServer server, ShoutVelocityBootstrap plugin, Runnable runnable) {
            super(runnable);
            this.server = server;
            this.plugin = plugin;
        }

        @Override
        public Task schedule() {
            ScheduledTask platformTask = this.server.getScheduler().buildTask(plugin, task)
                    .delay(this.delayInMillis, TimeUnit.MILLISECONDS)
                    .repeat(this.repeatInMillis, TimeUnit.MILLISECONDS)
                    .schedule();
            return platformTask::cancel;
        }
    }
}
