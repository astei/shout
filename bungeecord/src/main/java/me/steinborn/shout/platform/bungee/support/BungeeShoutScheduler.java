package me.steinborn.shout.platform.bungee.support;

import com.google.inject.Inject;
import me.steinborn.shout.platform.AbstractSchedulerTaskBuilder;
import me.steinborn.shout.platform.ShoutScheduler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class BungeeShoutScheduler implements ShoutScheduler, ShoutScheduler.SchedulerConfine {
    private final ProxyServer server;
    private final Plugin plugin;

    @Inject
    public BungeeShoutScheduler(ProxyServer server, Plugin plugin) {
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
        return new BungeeTaskBuilder(server, plugin, runnable);
    }

    private static class BungeeTaskBuilder extends AbstractSchedulerTaskBuilder {
        private final ProxyServer server;
        private final Plugin plugin;

        public BungeeTaskBuilder(ProxyServer server, Plugin plugin, Runnable runnable) {
            super(runnable);
            this.server = server;
            this.plugin = plugin;
        }

        @Override
        public Task schedule() {
            ScheduledTask task;
            if (this.repeatInMillis == 0) {
                task = this.server.getScheduler().schedule(this.plugin, this.task, this.delayInMillis, TimeUnit.MILLISECONDS);
            } else {
                task = this.server.getScheduler().schedule(this.plugin, this.task, this.delayInMillis, this.repeatInMillis, TimeUnit.MILLISECONDS);
            }
            return task::cancel;
        }
    }
}
