package me.steinborn.shout.platform.bungee.support;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BungeeShoutCommandInvoker implements ShoutCommandInvoker, ForwardingAudience {
    private final Audience audience;
    private final CommandSender sender;
    private final BungeeShoutPlatform platform;

    public BungeeShoutCommandInvoker(CommandSender sender, BungeeAudiences audiences, BungeeShoutPlatform platform) {
        this.platform = platform;
        this.audience = audiences.sender(sender);
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return ImmutableList.of(this.audience);
    }

    @Override
    public @Nullable ShoutPlayer asPlayer() {
        if (sender instanceof ProxiedPlayer) {
            return platform.player((ProxiedPlayer) sender);
        }
        return null;
    }
}
