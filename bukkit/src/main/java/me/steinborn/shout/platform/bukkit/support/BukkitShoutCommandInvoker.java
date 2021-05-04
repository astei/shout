package me.steinborn.shout.platform.bukkit.support;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitShoutCommandInvoker implements ShoutCommandInvoker, ForwardingAudience {
    private final Audience audience;
    private final CommandSender sender;
    private final BukkitShoutPlatform platform;

    public BukkitShoutCommandInvoker(CommandSender sender, BukkitAudiences audiences, BukkitShoutPlatform platform) {
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
        if (sender instanceof Player) {
            return platform.player((Player) sender);
        }
        return null;
    }
}
