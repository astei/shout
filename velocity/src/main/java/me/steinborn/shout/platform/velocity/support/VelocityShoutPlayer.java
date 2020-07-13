package me.steinborn.shout.platform.velocity.support;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.proxy.Player;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

public class VelocityShoutPlayer implements ShoutPlayer, ForwardingAudience {
    private final Player player;

    public VelocityShoutPlayer(Player player) {
        this.player = player;
    }

    @Override
    public UUID uuid() {
        return player.getUniqueId();
    }

    @Override
    public String username() {
        return player.getUsername();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public Optional<String> server() {
        return player.getCurrentServer().map(server -> server.getServerInfo().getName());
    }

    @Override
    public Optional<String> world() {
        return Optional.empty();
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return ImmutableList.of(this.player);
    }
}
