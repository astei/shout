package me.steinborn.shout.platform;

import net.kyori.adventure.audience.Audience;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents an in-game player. Wraps an {@link Audience]}.
 */
public interface ShoutPlayer extends Audience {
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
     * Determines if the user has the specified {@code permission}.
     *
     * @param permission the permission to check
     * @return {@code true} if the player has the permission, {@code false} otherwise
     */
    boolean hasPermission(String permission);

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
}
