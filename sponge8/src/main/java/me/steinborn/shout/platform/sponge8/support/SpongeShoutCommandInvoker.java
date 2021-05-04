package me.steinborn.shout.platform.sponge8.support;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identified;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;

public class SpongeShoutCommandInvoker<A extends Audience & Subject> implements ShoutCommandInvoker, ForwardingAudience {
    private final A sender;
    private final SpongeShoutPlatform platform;

    public SpongeShoutCommandInvoker(A sender, SpongeShoutPlatform platform) {
        this.platform = platform;
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return ImmutableList.of(this.sender);
    }

    @Override
    public @Nullable ShoutPlayer asPlayer() {
        if (sender instanceof Player) {
            return platform.player((Player) sender);
        }
        return null;
    }
}
