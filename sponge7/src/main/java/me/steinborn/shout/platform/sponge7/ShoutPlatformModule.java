package me.steinborn.shout.platform.sponge7;

import com.google.inject.*;
import me.steinborn.shout.platform.ConfigDir;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.sponge7.support.SpongeShoutPlatform;
import me.steinborn.shout.platform.sponge7.support.SpongeShoutScheduler;
import me.steinborn.shout.util.inject.ExactlyOnceProvider;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.nio.file.Path;

public class ShoutPlatformModule extends AbstractModule {
    private final Path dataDirectory;

    public ShoutPlatformModule(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected void configure() {
        // Shout support
        bind(new TypeLiteral<ShoutPlatform<CommandSource, Player>>() {}).toProvider(SpongeShoutPlatformProvider.class);
        bind(new TypeLiteral<ShoutPlatform<?, ?>>() {}).toProvider(SpongeShoutPlatformProvider.class);
        bind(ShoutScheduler.class).to(SpongeShoutScheduler.class);
        bind(Path.class).annotatedWith(ConfigDir.class).toInstance(dataDirectory);
    }

    @Provides
    private Server provideServer(Game game) {
        return game.getServer();
    }

    @Singleton
    private static class SpongeShoutPlatformProvider extends ExactlyOnceProvider<ShoutPlatform<CommandSource, Player>> {
        private final SpongeAudiences audiences;
        private final Server server;

        @Inject
        private SpongeShoutPlatformProvider(SpongeAudiences audiences, Server server) {
            super();
            this.audiences = audiences;
            this.server = server;
        }

        @Override
        protected ShoutPlatform<CommandSource, Player> actualProvide() {
            return new SpongeShoutPlatform(server, audiences);
        }
    }
}
