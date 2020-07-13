package me.steinborn.shout.platform.bukkit.support;

import com.google.inject.Inject;
import me.steinborn.shout.platform.AbstractSchedulerTaskBuilder;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.util.TimeUtil;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitShoutScheduler implements ShoutScheduler {
    private final SchedulerConfine serverConfine;
    private final SchedulerConfine asyncConfine;

    @Inject
    public BukkitShoutScheduler(Plugin plugin, Server server) {
        this.serverConfine = new BukkitSchedulerConfine(false, plugin, server);
        this.asyncConfine = new BukkitSchedulerConfine(true, plugin, server);
    }

    @Override
    public SchedulerConfine server() {
        return this.serverConfine;
    }

    @Override
    public SchedulerConfine async() {
        return this.asyncConfine;
    }

    private static class BukkitSchedulerConfine implements SchedulerConfine {
        private final boolean async;
        private final Plugin plugin;
        private final Server server;

        private BukkitSchedulerConfine(boolean async, Plugin plugin, Server server) {
            this.async = async;
            this.plugin = plugin;
            this.server = server;
        }

        @Override
        public TaskBuilder createTaskBuilder(Runnable runnable) {
            return new BukkitTaskBuilder(runnable, this.async, this.plugin, this.server);
        }
    }

    private static class BukkitTaskBuilder extends AbstractSchedulerTaskBuilder {
        private final boolean async;
        private final Plugin plugin;
        private final Server server;

        private BukkitTaskBuilder(Runnable task, boolean async, Plugin plugin, Server server) {
            super(task);
            this.async = async;
            this.plugin = plugin;
            this.server = server;
        }

        private BukkitTask platformSchedule() {
            if (this.async) {
                if (this.repeatInMillis == 0) {
                    return server.getScheduler().runTaskLaterAsynchronously(plugin, task, TimeUtil.toTicks(this.delayInMillis));
                } else {
                    return server.getScheduler().runTaskTimerAsynchronously(plugin, task, TimeUtil.toTicks(this.delayInMillis),
                            TimeUtil.toTicks(this.repeatInMillis));
                }
            } else {
                if (this.repeatInMillis == 0) {
                    return server.getScheduler().runTaskLater(plugin, task, TimeUtil.toTicks(this.delayInMillis));
                } else {
                    return server.getScheduler().runTaskTimer(plugin, task, TimeUtil.toTicks(this.delayInMillis),
                            TimeUtil.toTicks(this.repeatInMillis));
                }
            }
        }

        @Override
        public Task schedule() {
            BukkitTask platformTask = this.platformSchedule();
            return platformTask::cancel;
        }
    }
}
