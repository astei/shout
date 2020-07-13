package me.steinborn.shout.announcement;

import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.text.Component;

/**
 * Represents an announcement to be made to a player.
 */
public interface Announcement {
    /**
     * Returns the contents of the announcement to be sent.
     *
     * @return the content of the announcement
     */
    Component content();

    default boolean applicable(ShoutPlayer player) {
        return true;
    }
}
