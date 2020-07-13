package me.steinborn.shout.platform.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class ShoutBungeeBootstrap extends Plugin {
    @Override
    public void onEnable() {
        getLogger().info("Hey there, Shout isn't yet compatible with BungeeCord. Coming soon :)");
    }
}
