package me.steinborn.shout.platform.bungee;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.steinborn.shout.logic.ShoutCommonModule;
import me.steinborn.shout.logic.ShoutPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class ShoutBungeeBootstrap extends Plugin {
    @Inject
    private ShoutPlugin plugin;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new ShoutPlatformModule(this), new ShoutCommonModule());
        injector.injectMembers(this);

        plugin.onStart();
    }

    @Override
    public void onDisable() {
        plugin.onShutdown();
    }
}
