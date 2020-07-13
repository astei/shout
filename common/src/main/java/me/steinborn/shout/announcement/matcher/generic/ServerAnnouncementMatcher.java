package me.steinborn.shout.announcement.matcher.generic;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.announcement.matcher.AnnouncementMatcher;
import me.steinborn.shout.platform.ShoutPlayer;

import java.util.regex.Pattern;

public class ServerAnnouncementMatcher implements AnnouncementMatcher {
    private final ImmutableList<Pattern> servers;

    public ServerAnnouncementMatcher(ImmutableList<Pattern> servers) {
        this.servers = servers;
    }

    @Override
    public boolean satisfied(ShoutPlayer player) {
        return player.server()
                .filter(serverName -> anyMatch(serverName))
                .isPresent();
    }

    private boolean anyMatch(String serverName) {
        for (Pattern server : servers) {
            if (server.matcher(serverName).find()) {
                return true;
            }
        }
        return false;
    }
}
