package me.steinborn.shout.announcement.store;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.announcement.Announcement;

public class EmptyAnnouncementStore implements AnnouncementStore {
    @Override
    public ImmutableList<Announcement> fetchAnnouncements() {
        return ImmutableList.of();
    }
}
