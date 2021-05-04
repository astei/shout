package me.steinborn.shout.platform.sponge8.support;

import com.google.inject.*;
import java.nio.file.Path;
import me.steinborn.shout.platform.ConfigDir;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutScheduler;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

public class ShoutPlatformModule extends AbstractModule {
    private final Path dataDirectory;

    public ShoutPlatformModule(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected void configure() {
        // Shout support
        bind(new TypeLiteral<ShoutPlatform<Audience, Player>>() {}).toProvider(SpongeShoutPlatformProvider.class);
        bind(new TypeLiteral<ShoutPlatform<?, ?>>() {}).toProvider(SpongeShoutPlatformProvider.class);
        bind(ShoutScheduler.class).to(SpongeShoutScheduler.class);
        bind(Path.class).annotatedWith(ConfigDir.class).toInstance(dataDirectory);
    }

    @Provides
    private Server provideServer(Game game) {
        return game.server();
    }

    @Singleton
    private static class SpongeShoutPlatformProvider<A extends Audience & Subject> extends ExactlyOnceProvider<ShoutPlatform<A, ServerPlayer>> {
        private final Server server;

        @Inject
        private SpongeShoutPlatformProvider(Server server) {
            super();
            this.server = server;
        }

        @Override
        protected ShoutPlatform<A, ServerPlayer> actualProvide() {
            return new SpongeShoutPlatform<>(server);
        }
    }
}
