package me.steinborn.shout.announcement.matcher.generic;

import me.steinborn.shout.announcement.matcher.AnnouncementMatcher;
import me.steinborn.shout.platform.ShoutPlayer;

public class PermissionAnnouncementMatcher implements AnnouncementMatcher {
    private final String requiredPermission;
    private final boolean negated;

    public PermissionAnnouncementMatcher(String requiredPermission, boolean negated) {
        this.requiredPermission = requiredPermission;
        this.negated = negated;
    }

    @Override
    public boolean satisfied(ShoutPlayer player) {
        return player.hasPermission(this.requiredPermission) == !negated;
    }
}
