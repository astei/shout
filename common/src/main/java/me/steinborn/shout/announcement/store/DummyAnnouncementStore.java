package me.steinborn.shout.announcement.store;

import com.google.common.collect.ImmutableList;
import me.steinborn.shout.announcement.Announcement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class DummyAnnouncementStore implements AnnouncementStore {
    @Override
    public ImmutableList<Announcement> fetchAnnouncements() {
        return ImmutableList.of(
                new Announcement() {
                    @Override
                    public Component content() {
                        return TextComponent.of("Hi, this is just a test.");
                    }
                },
                new Announcement() {
                    @Override
                    public Component content() {
                        return TextComponent.of("Oh look, there's color.", NamedTextColor.YELLOW);
                    }
                },
                new Announcement() {
                    @Override
                    public Component content() {
                        return TextComponent.of("I've even got some RGB!", TextColor.of(0xff11a4));
                    }
                }
        );
    }
}
