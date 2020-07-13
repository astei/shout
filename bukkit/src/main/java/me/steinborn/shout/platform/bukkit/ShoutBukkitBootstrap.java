package me.steinborn.shout.platform.bukkit;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.steinborn.shout.logic.ShoutCommonModule;
import me.steinborn.shout.logic.ShoutPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ShoutBukkitBootstrap extends JavaPlugin {
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
