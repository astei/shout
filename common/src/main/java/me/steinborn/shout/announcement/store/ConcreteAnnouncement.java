package me.steinborn.shout.announcement.store;

import me.steinborn.shout.announcement.Announcement;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public class ConcreteAnnouncement implements Announcement {
    private final Component component;

    public ConcreteAnnouncement(Component component) {
        this.component = Objects.requireNonNull(component, "component");
    }

    @Override
    public Component content() {
        return this.component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteAnnouncement that = (ConcreteAnnouncement) o;
        return component.equals(that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(component);
    }
}
