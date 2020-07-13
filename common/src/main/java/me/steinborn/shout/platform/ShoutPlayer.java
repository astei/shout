package me.steinborn.shout.platform;

import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents an in-game player. Wraps an {@link Audience]}.
 */
public interface ShoutPlayer extends ShoutCommandInvoker {
    /**
     * Returns the player's UUID.
     *
     * @return the player's UUID
     */
    UUID uuid();

    /**
     * Returns the player's username.
     *
     * @return the player's UUID
     */
    String username();

    /**
     * Returns the server the player is on. If the player is still connecting, or Shout is not running on a proxy, this
     * will be empty.
     *
     * @return the server the player is on, if any
     */
    Optional<String> server();

    /**
     * Returns the world the player is in. If the player is still connecting, or Shout is not running on a server, this
     * will be empty.
     *
     * @return the world the player is in, if any
     */
    Optional<String> world();

    @Override
    @Nullable
    default ShoutPlayer asPlayer() {
        return this;
    }
}
