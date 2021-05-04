package me.steinborn.shout.platform.sponge8.support;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SpongeShoutPlayer implements ShoutPlayer, ForwardingAudience {
    private final ServerPlayer player;

    @Inject
    SpongeShoutPlayer(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public UUID uuid() {
        return player.uniqueId();
    }

    @Override
    public String username() {
        return player.name();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public Optional<String> server() {
        return Optional.empty();
    }

    @Override
    public Optional<String> world() {
        return Optional.of(player.world().key().asString());
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return ImmutableList.of(this.player);
    }
}
