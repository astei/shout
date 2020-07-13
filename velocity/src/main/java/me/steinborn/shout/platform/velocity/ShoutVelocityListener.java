package me.steinborn.shout.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import me.steinborn.shout.logic.ShoutPlugin;
import me.steinborn.shout.platform.ShoutPlatform;

public class ShoutVelocityListener {
    private final ShoutPlugin plugin;
    private final ShoutPlatform<Player> platform;

    @Inject
    public ShoutVelocityListener(ShoutPlugin plugin, ShoutPlatform<Player> platform) {
        this.plugin = plugin;
        this.platform = platform;
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        plugin.synchronize(platform.player(event.getPlayer()));
    }
}
