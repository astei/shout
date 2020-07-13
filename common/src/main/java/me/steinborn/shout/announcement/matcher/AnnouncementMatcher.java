package me.steinborn.shout.announcement.matcher;

import me.steinborn.shout.platform.ShoutPlayer;

public interface AnnouncementMatcher {
    boolean satisfied(ShoutPlayer player);
}
