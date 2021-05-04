package me.steinborn.shout.platform.sponge.support;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeShoutCommandInvoker implements ShoutCommandInvoker, ForwardingAudience {
    private final Audience audience;
    private final CommandSource sender;
    private final SpongeShoutPlatform platform;

    public SpongeShoutCommandInvoker(CommandSource sender, SpongeAudiences audiences, SpongeShoutPlatform platform) {
        this.platform = platform;
        this.audience = audiences.receiver(sender);
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
