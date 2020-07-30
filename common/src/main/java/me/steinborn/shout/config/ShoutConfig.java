package me.steinborn.shout.config;

import com.google.common.reflect.TypeToken;
import me.steinborn.shout.announcement.store.AnnouncementStore;
import me.steinborn.shout.announcement.store.flatfile.ConfigurateAnnouncementStore;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;

public class ShoutConfig {
    private final int interval;
    private final Component prefix;
    private final Storage storage;

    ShoutConfig(int interval, Component prefix, Storage storage) {
        this.interval = interval;
        this.prefix = prefix;
        this.storage = storage;
    }

    public static ShoutConfig load(Path configPath, ConfigurationNode node) {
        int interval = node.getNode("interval").getInt(10);
        ConfigurationNode prefix = node.getNode("chat").getNode("prefix");
        Storage storage = Storage.deserialize(node.getNode("storage"));
        return new ShoutConfig(interval, ComponentConfigurationUtil.deserialize(prefix), storage);
    }

    public int getInterval() {
        return interval;
    }

    public Component getPrefix() {
        return prefix;
    }

    public Storage getStorage() {
        return storage;
    }

    AnnouncementStore createAnnouncementStore(Path configPath) throws IOException {
        switch (storage.storageType) {
            case FLATFILE:
                ConfigurateAnnouncementStore store = new ConfigurateAnnouncementStore();
                store.load(configPath.resolve(storage.associatedConfig.getNode("path").getString("announcements.yml")));
                return store;
            default:
                throw new IllegalStateException("Unknown storage type: " + storage.storageType);
        }
    }

    static class Storage {
        private final StorageType storageType;
        private final ConfigurationNode associatedConfig;

        Storage(StorageType storageType, ConfigurationNode associatedConfig) {
            this.storageType = storageType;
            this.associatedConfig = associatedConfig;
        }

        public static Storage deserialize(ConfigurationNode node) {
            ConfigurationNode typeNode = node.getNode("type");
            StorageType type;
            try {
                type = typeNode.getValue(TypeToken.of(StorageType.class), StorageType.FLATFILE);
            } catch (ObjectMappingException e) {
                throw new RuntimeException("Storage type is invalid", e);
            }

            ConfigurationNode associated = node.getNode(type.name().toLowerCase(Locale.ENGLISH));
            return new Storage(type, associated);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Storage storage = (Storage) o;
            return storageType == storage.storageType &&
                    associatedConfig.equals(storage.associatedConfig);
        }

        @Override
        public int hashCode() {
            return Objects.hash(storageType, associatedConfig);
        }
    }

    enum StorageType {
        FLATFILE
    }

    enum AnnouncementDisplayType {
        CHAT,
        ACTION_BAR,
        BOSS_BAR,
        TITLE
    }
}
