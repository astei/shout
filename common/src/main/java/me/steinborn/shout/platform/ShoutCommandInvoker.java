package me.steinborn.shout.platform;

import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShoutCommandInvoker extends Audience {
    /**
     * Determines if the user has the specified {@code permission}.
     *
     * @param permission the permission to check
     * @return {@code true} if the player has the permission, {@code false} otherwise
     */
    boolean hasPermission(String permission);

    /**
     * Attempts to convert the {@code ShoutCommandInvoker} into a {@link ShoutPlayer}.
     *
     * @return the converted instance, or {@code null} if not convertable
     */
    default @Nullable ShoutPlayer asPlayer() {
        return null;
    }
}
