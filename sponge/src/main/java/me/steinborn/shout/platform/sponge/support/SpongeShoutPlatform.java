package me.steinborn.shout.platform.sponge.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Singleton
public class SpongeShoutPlatform implements ShoutPlatform<Player> {
    private final Server server;
    private final Map<Player, ShoutPlayer> playerMappings;
    private final Collection<ShoutPlayer> playerCollection;
    private final SpongeAudiences audiences;

    @Inject
    public SpongeShoutPlatform(Server server, SpongeAudiences audiences) {
        this.server = server;
        this.audiences = audiences;
        this.playerMappings = new MapMaker().weakKeys().makeMap();
        this.playerCollection = new AbstractCollection<ShoutPlayer>() {
            @Override
            public Iterator<ShoutPlayer> iterator() {
                Iterator<? extends Player> iterator = ImmutableList.copyOf(server.getOnlinePlayers()).iterator();
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
                return server.getOnlinePlayers().size();
            }
        };
    }

    @Override
    public Collection<ShoutPlayer> players() {
        return this.playerCollection;
    }

    @Override
    public PlatformVersion version() {
        PluginContainer implementation = Sponge.getGame().getPlatform().getContainer(Platform.Component.IMPLEMENTATION);
        return new PlatformVersion(implementation.getName(), implementation.getVersion().orElse("<UNKNOWN>"));
    }

    @Override
    public ShoutPlayer player(Player platformPlayer) {
        return playerMappings.computeIfAbsent(platformPlayer, ignored -> new SpongeShoutPlayer(audiences, platformPlayer));
    }
}
