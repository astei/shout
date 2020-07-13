package me.steinborn.shout.platform;

import java.util.Collection;

/**
 * Represents a platform upon which Shout runs on. Note that this class only contains utility auxiliary methods, and
 * does not include all services that must be provided by the platform.
 */
public interface ShoutPlatform<S, P extends S> {
    /**
     * Returns a list of all players connected to the server or proxy. This method is required to be thread-safe and
     * should represent a snapshot of all players online. Any {@link ShoutPlayer} instances contained within may be
     * initialized lazily but each player should result in a single {@code ShoutPlayer} per platform player, across
     * connections. The collection returned is not mutable.
     *
     * @return the list of players connected to the server or proxy
     */
    Collection<ShoutPlayer> players();

    /**
     * Returns a {@link ShoutPlayer} instance given a specified native player instance {@code player}.
     *
     * @param player the native player type
     * @return a {@link ShoutPlayer} that represents Shout's view of the native player
     */
    ShoutPlayer player(P player);

    /**
     * Returns a {@link ShoutCommandInvoker} instance that represents the console.
     *
     * @return a {@link ShoutCommandInvoker} instance that represents the console
     */
    ShoutCommandInvoker console();

    /**
     * Returns a {@link ShoutPlayer} instance given a specified native player instance {@code player}.
     *
     * @param sender the native sender
     * @return a {@link ShoutCommandInvoker} that represents Shout's view of the native sender
     */
    ShoutCommandInvoker invoker(S sender);

    /**
     * Returns the version of the platform Shout is running on.
     *
     * @return the version of the platform Shout is running on
     */
    PlatformVersion version();
}
