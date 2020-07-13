package me.steinborn.shout.platform.bungee.support;

import com.google.common.collect.Iterators;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class BungeeShoutPlatform implements ShoutPlatform<ProxiedPlayer> {
    private final ProxyServer server;
    private final Map<ProxiedPlayer, ShoutPlayer> playerMappings;
    private final BungeeAudiences audiences;

    @Inject
    public BungeeShoutPlatform(ProxyServer server, BungeeAudiences audiences) {
        this.server = server;
        this.audiences = audiences;
        this.playerMappings = new MapMaker().weakKeys().makeMap();
    }

    @Override
    public Collection<ShoutPlayer> players() {
        return new AbstractCollection<ShoutPlayer>() {
            @Override
            public Iterator<ShoutPlayer> iterator() {
                return Iterators.transform(server.getPlayers().iterator(), p -> player(p));
            }

            @Override
            public int size() {
                return server.getOnlineCount();
            }
        };
    }

    @Override
    public ShoutPlayer player(ProxiedPlayer player) {
        return playerMappings.computeIfAbsent(player, p -> new BungeeShoutPlayer(audiences, player));
    }

    @Override
    public PlatformVersion version() {
        return new PlatformVersion(this.server.getName(), this.server.getVersion());
    }
}
