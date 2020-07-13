package me.steinborn.shout.platform.velocity.support;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class VelocityShoutCommandInvoker implements ShoutCommandInvoker, ForwardingAudience {
    private final CommandSource sender;
    private final VelocityShoutPlatform platform;

    public VelocityShoutCommandInvoker(CommandSource sender, VelocityShoutPlatform platform) {
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
