package me.steinborn.shout.platform.bukkit.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class BukkitShoutPlatform implements ShoutPlatform<Player> {
    private final Server server;
    private final BukkitShoutPlayer.Factory playerFactory;
    private final Map<Player, ShoutPlayer> playerMappings;
    private final Collection<ShoutPlayer> playerCollection;

    @Inject
    public BukkitShoutPlatform(Server server, BukkitShoutPlayer.Factory playerFactory) {
        this.server = server;
        this.playerFactory = playerFactory;
        this.playerMappings = new MapMaker().weakKeys().makeMap();
        this.playerCollection = new AbstractCollection<ShoutPlayer>() {
            @Override
            public Iterator<ShoutPlayer> iterator() {
                Iterator<? extends Player> iterator = ImmutableList.copyOf(Bukkit.getOnlinePlayers()).iterator();
                return new Iterator<ShoutPlayer>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public ShoutPlayer next() {
                        return player(iterator.next());
                    }
                };
            }

            @Override
            public int size() {
                return Bukkit.getOnlinePlayers().size();
            }
        };
    }

    @Override
    public Collection<ShoutPlayer> players() {
        return this.playerCollection;
    }

    @Override
    public PlatformVersion version() {
        return new PlatformVersion(server.getName(), server.getBukkitVersion());
    }

    @Override
    public ShoutPlayer player(Player platformPlayer) {
        return playerMappings.computeIfAbsent(platformPlayer, playerFactory::create);
    }
}
