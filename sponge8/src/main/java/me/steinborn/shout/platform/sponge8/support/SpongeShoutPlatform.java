package me.steinborn.shout.platform.sponge8.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.plugin.PluginContainer;

@Singleton
public class SpongeShoutPlatform<A extends Audience & Subject> implements ShoutPlatform<A, ServerPlayer> {
    private final Server server;
    private final Map<Player, ShoutPlayer> playerMappings;
    private final Collection<ShoutPlayer> playerCollection;

    @Inject
    public SpongeShoutPlatform(Server server) {
        this.server = server;
        this.playerMappings = new MapMaker().weakKeys().makeMap();
        this.playerCollection = new AbstractCollection<ShoutPlayer>() {
            @Override
            public Iterator<ShoutPlayer> iterator() {
                Iterator<ServerPlayer> iterator = ImmutableList.copyOf(server.onlinePlayers()).iterator();
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
                return server.onlinePlayers().size();
            }
        };
    }

    @Override
    public Collection<ShoutPlayer> players() {
        return this.playerCollection;
    }

    @Override
    public PlatformVersion version() {
        PluginContainer implementation = Sponge.game().platform().container(Platform.Component.IMPLEMENTATION);
        return new PlatformVersion(implementation.metadata().name().orElse(implementation.metadata().id()),
                implementation.metadata().version());
    }

    @Override
    public ShoutPlayer player(ServerPlayer platformPlayer) {
        return playerMappings.computeIfAbsent(platformPlayer, ignored -> new SpongeShoutPlayer(platformPlayer));
    }

    @Override
    public ShoutCommandInvoker console() {
        return this.invoker((A) this.server.game().systemSubject());
    }

    @Override
    public ShoutCommandInvoker invoker(A sender) {
        if (sender instanceof ServerPlayer) {
            return this.player((ServerPlayer) sender);
        }
        return new SpongeShoutCommandInvoker<>(sender, this);
    }
}
