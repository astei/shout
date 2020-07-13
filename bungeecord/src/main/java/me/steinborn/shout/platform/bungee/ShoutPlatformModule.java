package me.steinborn.shout.platform.bungee;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import me.steinborn.shout.platform.ConfigDir;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.bungee.support.BungeeShoutPlatform;
import me.steinborn.shout.platform.bungee.support.BungeeShoutScheduler;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.impl.JDK14LoggerAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

public class ShoutPlatformModule extends AbstractModule {
    private final ShoutBungeeBootstrap plugin;

    public ShoutPlatformModule(ShoutBungeeBootstrap plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        // Basic BungeeCord bindings
        bind(ProxyServer.class).toInstance(plugin.getProxy());
        bind(Plugin.class).toInstance(plugin);

        // Use a global audiences instance
        bind(BungeeAudiences.class).toInstance(BungeeAudiences.create(plugin));

        // Shout support
        bind(new TypeLiteral<ShoutPlatform<?>>() {}).to(BungeeShoutPlatform.class);
        bind(new TypeLiteral<ShoutPlatform<ProxiedPlayer>>() {}).to(BungeeShoutPlatform.class);
        bind(ShoutScheduler.class).to(BungeeShoutScheduler.class);
        bind(Path.class).annotatedWith(ConfigDir.class).toInstance(plugin.getDataFolder().toPath());
    }

    @Provides
    @Singleton
    public Logger createLogger() {
        // See if we can use the SLF4J logger provided by Waterfall
        try {
            //noinspection JavaReflectionMemberAccess
            Method method = Plugin.class.getDeclaredMethod("getSLF4JLogger");
            method.setAccessible(true);
            Logger logger = (Logger) method.invoke(plugin);
            logger.info("Using Waterfall SLF4J logger");
            return logger;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Assume that Paper getSLF4JLogger() is not available
            plugin.getLogger().warning("Waterfall SLF4J logger unavailable, falling back to generic wrapper. Please consider using Waterfall instead.");
            try {
                Constructor<JDK14LoggerAdapter> adapterConstructor = JDK14LoggerAdapter.class.getDeclaredConstructor(java.util.logging.Logger.class);
                adapterConstructor.setAccessible(true);
                return adapterConstructor.newInstance(plugin.getLogger());
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e2) {
                throw new RuntimeException("Unable to initialize SLF4J logging adapter. We cannot continue!");
            }
        }
    }
}
