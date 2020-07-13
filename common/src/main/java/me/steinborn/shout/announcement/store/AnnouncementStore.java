package me.steinborn.shout.announcement.store;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.announcement.Announcement;

public interface AnnouncementStore {
    ImmutableList<Announcement> fetchAnnouncements();
}
