package me.steinborn.shout.platform.velocity;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.proxy.Player;
import me.steinborn.shout.platform.ConfigDir;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutScheduler;
import me.steinborn.shout.platform.velocity.support.VelocityShoutPlatform;
import me.steinborn.shout.platform.velocity.support.VelocityShoutScheduler;

import java.nio.file.Path;

public class ShoutPlatformModule extends AbstractModule {
    private final Path dataDirectory;

    public ShoutPlatformModule(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected void configure() {
        // Shout support
        bind(new TypeLiteral<ShoutPlatform<?>>() {}).to(VelocityShoutPlatform.class);
        bind(new TypeLiteral<ShoutPlatform<Player>>() {}).to(VelocityShoutPlatform.class);
        bind(ShoutScheduler.class).to(VelocityShoutScheduler.class);
        bind(Path.class).annotatedWith(ConfigDir.class).toInstance(dataDirectory);
    }
}
