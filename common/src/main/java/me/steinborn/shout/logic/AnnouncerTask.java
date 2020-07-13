package me.steinborn.shout.logic;

import com.google.inject.Inject;
import me.steinborn.shout.announcement.Announcement;
import me.steinborn.shout.announcement.chain.AnnouncementChain;
import me.steinborn.shout.announcement.chain.AnnouncementChainStorage;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;

public class AnnouncerTask implements Runnable {
    private final ShoutPlatform<?> platform;
    private final AnnouncementChainStorage chainStorage;

    @Inject
    public AnnouncerTask(ShoutPlatform<?> platform,
                         AnnouncementChainStorage chainStorage) {
        this.platform = platform;
        this.chainStorage = chainStorage;
    }

    @Override
    public void run() {
        for (ShoutPlayer player : platform.players()) {
            AnnouncementChain chain = chainStorage.getOrCreateChain(player);
            Announcement announcement = chain.getAndUpdateNext();
            if (announcement == null) {
                continue;
            }
            player.sendMessage(announcement.content());
        }
    }
}
