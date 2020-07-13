package me.steinborn.shout.announcement.chain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.steinborn.shout.announcement.Announcement;
import me.steinborn.shout.config.ConfigurationHolder;
import me.steinborn.shout.platform.ShoutPlayer;
import me.steinborn.shout.announcement.store.AnnouncementStore;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class AnnouncementChainStorage {
    private final ConfigurationHolder configHolder;
    private final Map<ShoutPlayer, AnnouncementChain> chains;
    private final AtomicReference<ImmutableList<Announcement>> cachedAnnouncements;

    @Inject
    public AnnouncementChainStorage(ConfigurationHolder configHolder) {
        this.configHolder = configHolder;
        this.chains = new MapMaker().weakKeys().makeMap();
        this.cachedAnnouncements = new AtomicReference<>(ImmutableList.of());
    }

    public void refreshAnnouncements() {
        ImmutableList<Announcement> announcements = configHolder.announcementStore().fetchAnnouncements();
        cachedAnnouncements.set(announcements);
        for (AnnouncementChain chain : chains.values()) {
            chain.synchronize(announcements);
        }
    }

    public AnnouncementChain getOrCreateChain(ShoutPlayer player) {
        return chains.computeIfAbsent(player, sp -> new AnnouncementChain(sp, cachedAnnouncements.get()));
    }

    public void synchronizeChain(ShoutPlayer player) {
        getOrCreateChain(player).synchronize(this.cachedAnnouncements.get());
    }

    public void removeChain(ShoutPlayer player) {
        chains.remove(player);
    }
}
