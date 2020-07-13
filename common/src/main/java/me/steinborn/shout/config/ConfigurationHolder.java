package me.steinborn.shout.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.steinborn.shout.announcement.store.AnnouncementStore;
import me.steinborn.shout.announcement.store.EmptyAnnouncementStore;
import me.steinborn.shout.platform.ConfigDir;
import net.kyori.adventure.text.TextComponent;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ConfigurationHolder {
    private final AtomicReference<ShoutConfig> config;
    private final AtomicReference<AnnouncementStore> announcementStore;
    private final Path configPath;

    @Inject
    public ConfigurationHolder(@ConfigDir Path configPath) {
        this.config = new AtomicReference<>();
        this.announcementStore = new AtomicReference<>();
        this.configPath = configPath;
    }

    public ShoutConfig config() {
        ShoutConfig config = this.config.get();
        if (config == null) {
            throw new IllegalStateException("Shout configuration not initialized.");
        }
        return config;
    }

    public AnnouncementStore announcementStore() {
        AnnouncementStore announcementStore = this.announcementStore.get();
        if (announcementStore == null) {
            throw new IllegalStateException("Shout announcement store not initialized.");
        }
        return announcementStore;
    }

    private void setConfig(ShoutConfig config) {
        this.config.set(config);
    }

    private void setAnnouncementStore(AnnouncementStore announcementStore) {
        this.announcementStore.set(announcementStore);
    }

    public void loadConfig() throws IOException {
        if (!Files.exists(this.configPath)) {
            Files.createDirectories(this.configPath);
        }

        Path configFilePath = this.configPath.resolve("config.yml");
        if (!Files.exists(configFilePath)) {
            Files.copy(ConfigurationHolder.class.getResourceAsStream("/defaults/config.yml"), configFilePath);
            Path defaultAnnouncements = configPath.resolve("announcements.yml");
            if (!Files.exists(defaultAnnouncements)) {
                Files.copy(ConfigurationHolder.class.getResourceAsStream("/defaults/announcements.yml"), defaultAnnouncements);
            }
        }
        YAMLConfigurationLoader configurationLoader = YAMLConfigurationLoader.builder()
                .setIndent(2)
                .setPath(configFilePath)
                .build();
        ShoutConfig config = ShoutConfig.load(this.configPath, configurationLoader.load());

        ShoutConfig existingConfig = this.config.get();
        this.setConfig(config);
        if (existingConfig == null || !existingConfig.getStorage().equals(config.getStorage())) {
            this.setAnnouncementStore(config.createAnnouncementStore(configPath));
        }
    }

    public void failsafe() {
        ShoutConfig failsafeConfig = new ShoutConfig(Integer.MAX_VALUE, TextComponent.empty(),
                new ShoutConfig.Storage(ShoutConfig.StorageType.FLATFILE, SimpleConfigurationNode.root().setValue("dummy.yml")));
        AnnouncementStore fakeStore = new EmptyAnnouncementStore();

        this.setConfig(failsafeConfig);
        this.setAnnouncementStore(fakeStore);
    }
}
