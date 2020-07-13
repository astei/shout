package me.steinborn.shout.logic;

import com.google.inject.Inject;
import me.steinborn.shout.announcement.Announcement;
import me.steinborn.shout.announcement.chain.AnnouncementChain;
import me.steinborn.shout.announcement.chain.AnnouncementChainStorage;
import me.steinborn.shout.config.ConfigurationHolder;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class AnnouncerTask implements Runnable {
    private final ShoutPlatform<?, ?> platform;
    private final ConfigurationHolder configurationHolder;
    private final AnnouncementChainStorage chainStorage;

    @Inject
    public AnnouncerTask(ShoutPlatform<?, ?> platform,
                         ConfigurationHolder configurationHolder,
                         AnnouncementChainStorage chainStorage) {
        this.platform = platform;
        this.configurationHolder = configurationHolder;
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
            player.sendMessage(formatAnnouncement(announcement, player));
        }
    }

    private Component formatAnnouncement(Announcement announcement, ShoutPlayer player) {
        return TextComponent.builder()
                .append(configurationHolder.config().getPrefix())
                .append(announcement.content())
                .build();
    }
}
