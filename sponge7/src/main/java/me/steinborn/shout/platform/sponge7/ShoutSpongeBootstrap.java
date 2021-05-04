package me.steinborn.shout.platform.sponge7;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.steinborn.shout.logic.ShoutCommonModule;
import me.steinborn.shout.logic.ShoutPlugin;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(id = "shout",
        name = "Shout",
        version = "0.1",
        authors = {"tuxed"},
        dependencies = {
            @Dependency(id = "viaversion", optional = true)
        })
public class ShoutSpongeBootstrap {
    private final Injector parentInjector;
    private final Path dataDirectory;

    private ShoutPlugin plugin;

    @Inject
    public ShoutSpongeBootstrap(Injector parentInjector, @ConfigDir(sharedRoot = false) Path dataDirectory) {
        this.parentInjector = parentInjector;
        this.dataDirectory = dataDirectory;
    }

    @Listener
    public void onGameStartingServer(GameStartingServerEvent event) {
        Injector injector = parentInjector.createChildInjector(new ShoutPlatformModule(dataDirectory), new ShoutCommonModule());
        this.plugin = injector.getInstance(ShoutPlugin.class);
        plugin.onStart();
    }

    @Listener
    public void onGameStopping(GameStoppingEvent event) {
        plugin.onShutdown();
    }
}
