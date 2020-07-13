package me.steinborn.shout.announcement.store.flatfile;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import me.steinborn.shout.announcement.Announcement;
import me.steinborn.shout.announcement.store.AnnouncementStore;
import me.steinborn.shout.announcement.store.ConcreteAnnouncement;
import me.steinborn.shout.config.ComponentConfigurationUtil;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Reads all announcements from a flat file. The file can be any format supported by Configurate.
 */
public class ConfigurateAnnouncementStore implements AnnouncementStore {
    private final AtomicReference<ConfigurationNode> config;
    private final AtomicReference<ImmutableList<Announcement>> announcements;
    private ConfigurationLoader<ConfigurationNode> loader;

    public ConfigurateAnnouncementStore() {
        this.config = new AtomicReference<>();
        this.announcements = new AtomicReference<>(ImmutableList.of());
    }

    public void load(Path path) throws IOException {
        SupportedFormat guessed = guessFormat(path);
        if (guessed == null) {
            throw new IllegalArgumentException("Invalid format");
        }

        ConfigurationLoader<ConfigurationNode> loader;
        switch (guessed) {
            case YAML:
                loader = YAMLConfigurationLoader.builder()
                        .setPath(path)
                        .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
                        .setIndent(2)
                        .build();
                break;
            case JSON:
                loader = GsonConfigurationLoader.builder()
                        .setPath(path)
                        .setIndent(4)
                        .build();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + guessed);
        }

        this.config.set(loader.load());
        this.announcements.set(deserialize(config.get()));
        this.loader = loader;
    }

    private static ImmutableList<Announcement> deserialize(ConfigurationNode root) {
        ConfigurationNode announcements = root.getNode("announcements");
        if (!announcements.hasListChildren()) {
            return ImmutableList.of();
        }

        ImmutableList.Builder<Announcement> announcementList = ImmutableList.builder();
        for (ConfigurationNode announcement : announcements.getChildrenList()) {
            ConfigurationNode contentNode = announcement.getNode("content");
            Component contentComponent = ComponentConfigurationUtil.deserialize(contentNode);

            announcementList.add(new ConcreteAnnouncement(contentComponent));
        }

        return announcementList.build();
    }

    private static @Nullable SupportedFormat guessFormat(Path path) {
        SupportedFormat guessed = null;
        for (SupportedFormat format : SupportedFormat.values()) {
            if (format.couldBeFormat(path)) {
                guessed = format;
                break;
            }
        }
        return guessed;
    }

    @Override
    public ImmutableList<Announcement> fetchAnnouncements() {
        return this.announcements.get();
    }

    public enum SupportedFormat {
        YAML(ImmutableList.of(".yml", ".yaml")),
        JSON(ImmutableList.of(".json"));

        private final ImmutableList<String> extensions;

        SupportedFormat(ImmutableList<String> extensions) {
            this.extensions = extensions;
        }

        public boolean couldBeFormat(Path path) {
            for (String extension : extensions) {
                if (path.getFileName().toString().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }
    }
}
