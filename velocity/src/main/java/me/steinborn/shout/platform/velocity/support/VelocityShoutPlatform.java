package me.steinborn.shout.platform.velocity.support;

import com.google.common.collect.Iterators;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ProxyVersion;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class VelocityShoutPlatform implements ShoutPlatform<CommandSource, Player> {
    private final ProxyServer server;
    private final Map<Player, ShoutPlayer> playerMappings;

    @Inject
    public VelocityShoutPlatform(ProxyServer server) {
        this.server = server;
        this.playerMappings = new MapMaker().weakKeys().makeMap();
    }

    @Override
    public Collection<ShoutPlayer> players() {
        return new AbstractCollection<ShoutPlayer>() {
            @Override
            public Iterator<ShoutPlayer> iterator() {
                return Iterators.transform(server.getAllPlayers().iterator(), p -> player(p));
            }

            @Override
            public int size() {
                return server.getPlayerCount();
            }
        };
    }

    @Override
    public ShoutPlayer player(Player player) {
        return playerMappings.computeIfAbsent(player, VelocityShoutPlayer::new);
    }

    @Override
    public ShoutCommandInvoker console() {
        return this.invoker(this.server.getConsoleCommandSource());
    }

    @Override
    public ShoutCommandInvoker invoker(CommandSource sender) {
        if (sender instanceof Player) {
            return this.player((Player) sender);
        } else {
            return new VelocityShoutCommandInvoker(sender, this);
        }
    }

    @Override
    public PlatformVersion version() {
        ProxyVersion version = this.server.getVersion();
        return new PlatformVersion(version.getName(), version.getVersion());
    }
}
