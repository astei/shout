package me.steinborn.shout.platform.bukkit;

import com.google.inject.*;
import me.steinborn.shout.platform.ConfigDir;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.bukkit.support.BukkitShoutPlatform;
import me.steinborn.shout.platform.bukkit.support.BukkitShoutPlayer;
import me.steinborn.shout.platform.bukkit.support.BukkitShoutScheduler;
import me.steinborn.shout.util.inject.ExactlyOnceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.impl.JDK14LoggerAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

public class ShoutPlatformModule extends AbstractModule {
    private final ShoutBukkitBootstrap plugin;

    public ShoutPlatformModule(ShoutBukkitBootstrap plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        // Basic Bukkit bindings
        bind(Server.class).toInstance(plugin.getServer());
        bind(Plugin.class).toInstance(plugin);

        // Use a global audiences instance
        bind(BukkitAudiences.class).toInstance(BukkitAudiences.create(plugin));

        // Shout support
        bind(new TypeLiteral<ShoutPlatform<?, ?>>() {}).toProvider(BukkitShoutPlatformProvider.class);
        bind(new TypeLiteral<ShoutPlatform<CommandSender, Player>>() {}).toProvider(BukkitShoutPlatformProvider.class);
        bind(ShoutScheduler.class).to(BukkitShoutScheduler.class);
        bind(Path.class).annotatedWith(ConfigDir.class).toInstance(plugin.getDataFolder().toPath());
    }

    @Provides
    @Singleton
    public Logger createLogger() {
        // See if we can use the SLF4J logger provided by Paper
        try {
            //noinspection JavaReflectionMemberAccess
            Method method = Plugin.class.getDeclaredMethod("getSLF4JLogger");
            method.setAccessible(true);
            Logger logger = (Logger) method.invoke(plugin);
            logger.info("Using Paper SLF4J logger");
            return logger;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Assume that Paper getSLF4JLogger() is not available
            plugin.getLogger().warning("Paper SLF4J logger unavailable, falling back to generic wrapper. Please consider using Paper instead.");
            try {
                Constructor<JDK14LoggerAdapter> adapterConstructor = JDK14LoggerAdapter.class.getDeclaredConstructor(java.util.logging.Logger.class);
                adapterConstructor.setAccessible(true);
                return adapterConstructor.newInstance(plugin.getLogger());
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e2) {
                throw new RuntimeException("Unable to initialize SLF4J logging adapter. We cannot continue!");
            }
        }
    }


    @Singleton
    private static class BukkitShoutPlatformProvider extends ExactlyOnceProvider<ShoutPlatform<CommandSender, Player>> {
        private final BukkitAudiences audiences;
        private final Server server;

        @Inject
        private BukkitShoutPlatformProvider(BukkitAudiences audiences, Server server) {
            super();
            this.audiences = audiences;
            this.server = server;
        }

        @Override
        protected ShoutPlatform<CommandSender, Player> actualProvide() {
            return new BukkitShoutPlatform(server, audiences);
        }
    }
}
