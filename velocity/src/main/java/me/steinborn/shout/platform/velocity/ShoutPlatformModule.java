package me.steinborn.shout.platform.velocity;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.steinborn.shout.platform.ConfigDir;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.velocity.support.VelocityShoutPlatform;
import me.steinborn.shout.platform.velocity.support.VelocityShoutScheduler;
import me.steinborn.shout.util.inject.ExactlyOnceProvider;

import java.nio.file.Path;

public class ShoutPlatformModule extends AbstractModule {
    private final Path dataDirectory;

    public ShoutPlatformModule(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected void configure() {
        // Shout support
        bind(new TypeLiteral<ShoutPlatform<?, ?>>() {}).toProvider(VelocityShoutPlatformProvider.class);
        bind(new TypeLiteral<ShoutPlatform<CommandSource, Player>>() {}).toProvider(VelocityShoutPlatformProvider.class);
        bind(ShoutScheduler.class).to(VelocityShoutScheduler.class);
        bind(Path.class).annotatedWith(ConfigDir.class).toInstance(dataDirectory);
    }

    @Singleton
    private static class VelocityShoutPlatformProvider extends ExactlyOnceProvider<ShoutPlatform<CommandSource, Player>> {
        private final ProxyServer server;

        @Inject
        private VelocityShoutPlatformProvider(ProxyServer server) {
            super();
            this.server = server;
        }

        @Override
        protected ShoutPlatform<CommandSource, Player> actualProvide() {
            return new VelocityShoutPlatform(server);
        }
    }
}
