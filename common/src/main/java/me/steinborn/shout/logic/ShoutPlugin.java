package me.steinborn.shout.logic;

import com.google.inject.Inject;
import me.steinborn.shout.announcement.chain.AnnouncementChainStorage;
import me.steinborn.shout.config.ConfigurationHolder;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import me.steinborn.shout.platform.ShoutScheduler;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class ShoutPlugin {
    private final ConfigurationHolder configurationHolder;
    private final ShoutPlatform<?> platform;
    private final ShoutScheduler scheduler;
    private final Logger logger;
    private final AnnouncementChainStorage announcementChainStorage;
    private final AnnouncerTask announcerTask;

    private ShoutScheduler.Task platformAnnouncerTask;

    @Inject
    public ShoutPlugin(ConfigurationHolder configurationHolder, ShoutPlatform<?> platform, ShoutScheduler scheduler,
                       Logger logger, AnnouncementChainStorage announcementChainStorage, AnnouncerTask announcerTask) {
        this.configurationHolder = configurationHolder;
        this.platform = platform;
        this.scheduler = scheduler;
        this.logger = logger;
        this.announcementChainStorage = announcementChainStorage;
        this.announcerTask = announcerTask;
    }

    public void synchronize(ShoutPlayer player) {
        this.announcementChainStorage.synchronizeChain(player);
    }

    public void onStart() {
        this.printGreeting();

        try {
            this.configurationHolder.loadConfig();
        } catch (Exception e) {
            this.logger.error("/!\\ /!\\ /!\\ UNABLE TO LOAD YOUR SHOUT CONFIGURATION /!\\ /!\\ /!\\");
            this.logger.error("You made a mistake whilst modifying the Shout configuration. The plugin is now in fail-safe mode.");
            this.logger.error("No announcements will be displayed and the only valid action is to reload the plugin.");
            this.logger.error("Please correct the configuration issue and reload the plugin configuration.");
            this.logger.error("For the record, the error we came across whilst loading your config was:", e);
            this.logger.error("/!\\ /!\\ /!\\ UNABLE TO LOAD YOUR SHOUT CONFIGURATION /!\\ /!\\ /!\\");

            this.configurationHolder.failsafe();
        }

        this.announcementChainStorage.refreshAnnouncements();
        this.startAnnouncer();
    }

    private void startAnnouncer() {
        this.platformAnnouncerTask = this.scheduler.server().createTaskBuilder(new TrickleTimer(10, announcerTask))
                .delay(1, TimeUnit.SECONDS)
                .repeat(1, TimeUnit.SECONDS)
                .schedule();
        this.logger.info("Announcer task started.");
    }

    private void printGreeting() {
        PlatformVersion version = this.platform.version();

        // Print a pretty figlet
        logger.info("   _____ __                __  __");
        logger.info("  / ___// /_  ____  __  __/ /_/ /");
        logger.info("  \\__ \\/ __ \\/ __ \\/ / / / __/ /   Running on " + version.getName() + " " + version.getVersion());
        logger.info(" ___/ / / / / /_/ / /_/ / /_/_/    Shout 0.1");
        logger.info("/____/_/ /_/\\____/\\__,_/\\__(_)   ");
        logger.info("");
    }

    public void onShutdown() {
        if (this.platformAnnouncerTask != null) {
            this.platformAnnouncerTask.cancel();
        }
    }
}
