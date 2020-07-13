package me.steinborn.shout.platform.bukkit.support;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

public class BukkitShoutPlayer implements ShoutPlayer, ForwardingAudience {
    private final Audience wrappedAudience;
    private final Player player;

    @Inject
    private BukkitShoutPlayer(BukkitAudiences audiences, @Assisted Player player) {
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
        return Optional.of(player.getWorld().getName());
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return ImmutableList.of(this.wrappedAudience);
    }

    public interface Factory {
        ShoutPlayer create(Player player);
    }
}
