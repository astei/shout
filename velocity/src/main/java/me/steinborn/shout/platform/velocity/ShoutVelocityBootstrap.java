package me.steinborn.shout.platform.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.steinborn.shout.logic.ShoutCommonModule;
import me.steinborn.shout.logic.ShoutPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "shout", name = "Shout", version = "0.1", authors = {"tuxed"})
public class ShoutVelocityBootstrap {
    private final ProxyServer server;
    private final Injector parentInjector;
    private final Logger logger;
    private ShoutPlugin plugin;
    private ShoutVelocityListener listener;
    private final Path dataDirectory;

    @Inject
    public ShoutVelocityBootstrap(ProxyServer server, Injector parentInjector, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.parentInjector = parentInjector;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        // Check for a class that only exists in Velocity 1.1.0 and above
        try {
            Class.forName("com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent");
        } catch (ClassNotFoundException e) {
            logger.error("Shout doesn't work on Velocity 1.0.x - please upgrade to Velocity 1.1.0.");
            return;
        }

        Injector injector = parentInjector.createChildInjector(new ShoutPlatformModule(dataDirectory), new ShoutCommonModule());
        this.plugin = injector.getInstance(ShoutPlugin.class);
        this.listener = injector.getInstance(ShoutVelocityListener.class);
        plugin.onStart();

        server.getEventManager().register(this, this.listener);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        plugin.onShutdown();
    }
}
