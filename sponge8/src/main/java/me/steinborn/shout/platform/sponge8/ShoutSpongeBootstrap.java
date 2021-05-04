package me.steinborn.shout.platform.sponge8;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.nio.file.Path;
import me.steinborn.shout.logic.ShoutCommonModule;
import me.steinborn.shout.logic.ShoutPlugin;
import me.steinborn.shout.platform.sponge8.support.ShoutPlatformModule;
import org.spongepowered.api.Server;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin("shout")
public class ShoutSpongeBootstrap {
    private final Injector parentInjector;
    private final Path dataDirectory;
    private final PluginContainer container;

    private ShoutPlugin plugin;

    @Inject
    public ShoutSpongeBootstrap(Injector parentInjector, @ConfigDir(sharedRoot = false) Path dataDirectory,
                                PluginContainer container) {
        this.parentInjector = parentInjector;
        this.dataDirectory = dataDirectory;
        this.container = container;
    }

    @Listener
    public void onServerStart(StartedEngineEvent<Server> event) {
        Injector injector = parentInjector.createChildInjector(new ShoutPlatformModule(dataDirectory), new ShoutCommonModule());
        this.plugin = injector.getInstance(ShoutPlugin.class);
        plugin.onStart();
    }

    @Listener
    public void onServerStop(StoppingEngineEvent<Server> event) {
        plugin.onShutdown();
    }

    public PluginContainer getContainer() {
        return container;
    }
}
