package me.steinborn.shout.platform.bungee.support;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

class BungeeShoutPlayer implements ShoutPlayer, ForwardingAudience {
    private final Audience wrappedAudience;
    private final ProxiedPlayer player;

    BungeeShoutPlayer(BungeeAudiences audiences, ProxiedPlayer player) {
        this.player = player;
        this.wrappedAudience = audiences.player(player);
    }

    @Override
    public UUID uuid() {
        return player.getUniqueId();
    }

    @Override
    public String username() {
        return player.getName();
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
        return Optional.empty();
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return ImmutableList.of(this.wrappedAudience);
    }
}
