package me.steinborn.shout.announcement.chain;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.announcement.Announcement;
import me.steinborn.shout.platform.ShoutPlayer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Provides a "chain" of announcements for a player. This allows us to reduce work done on each announcement (since the
 * operation is synchronous) and allows preserving order in announcements. Although intended for an synchronous
 * environment, this class must necessarily be thread-safe because other platforms do not have a main thread and even
 * with a main thread, announcement matchers may be updated at any time asynchronously.
 */
public class AnnouncementChain {
    private final ShoutPlayer player;
    private ImmutableList<Announcement> applicable;
    private int currentAnnouncementIndex;
    private final Object chainLock = new Object();

    public AnnouncementChain(ShoutPlayer player, ImmutableList<Announcement> applicable) {
        this.player = player;
        this.applicable = applicable;
    }

    public @Nullable Announcement getAndUpdateNext() {
        synchronized (chainLock) {
            if (applicable.isEmpty()) {
                return null;
            }
            Announcement announcement = this.applicable.get(this.currentAnnouncementIndex);

            this.currentAnnouncementIndex++;
            if (this.currentAnnouncementIndex >= this.applicable.size()) {
                this.currentAnnouncementIndex = 0;
            }

            return announcement;
        }
    }

    public void synchronize(List<Announcement> newAnnouncements) {
        ImmutableList.Builder<Announcement> applicableBuilder = ImmutableList.builder();
        for (Announcement announcement : newAnnouncements) {
            if (announcement.applicable(this.player)) {
                applicableBuilder.add(announcement);
            }
        }
        ImmutableList<Announcement> newApplicable = applicableBuilder.build();

        synchronized (chainLock) {
            if (this.applicable.isEmpty()) {
                this.applicable = newApplicable;
                this.currentAnnouncementIndex = 0;
            } else {
                int indexOfCurrentAnnouncement = newApplicable.indexOf(this.applicable.get(this.currentAnnouncementIndex));
                if (indexOfCurrentAnnouncement == -1) {
                    // Doesn't exist any longer. The best fallback is to start over.
                    indexOfCurrentAnnouncement = 0;
                }
                this.applicable = newApplicable;
                this.currentAnnouncementIndex = indexOfCurrentAnnouncement;
            }
        }
    }
}
