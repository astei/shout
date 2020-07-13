package me.steinborn.shout.platform.sponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "shout",
        name = "Shout",
        version = "0.1",
        authors = {"tuxed"},
        dependencies = {
            @Dependency(id = "viaversion", optional = true)
        })
public class ShoutSpongeBootstrap {
    private final Server server;
    private final Injector injector;
    private final Logger logger;

    @Inject
    public ShoutSpongeBootstrap(Server server, Injector injector, Logger logger) {
        this.server = server;
        this.injector = injector;
        this.logger = logger;
    }

    @Listener
    public void onGameStartingServer(GameStartingServerEvent event) {
        logger.info("Hey there, Shout isn't yet compatible with Sponge. Coming soon :)");
    }
}
