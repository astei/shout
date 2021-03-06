package me.steinborn.shout.platform.bukkit.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import me.steinborn.shout.platform.PlatformVersion;
import me.steinborn.shout.platform.ShoutCommandInvoker;
import me.steinborn.shout.platform.ShoutPlatform;
import me.steinborn.shout.platform.ShoutPlayer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class BukkitShoutPlatform implements ShoutPlatform<CommandSender, Player> {
    private final Server server;
    private final BukkitAudiences audiences;
    private final Map<Player, ShoutPlayer> playerMappings;
    private final Collection<ShoutPlayer> playerCollection;

    @Inject
    public BukkitShoutPlatform(Server server, BukkitAudiences audiences) {
        this.server = server;
        this.audiences = audiences;
        this.playerMappings = new MapMaker().weakKeys().makeMap();
        this.playerCollection = new AbstractCollection<ShoutPlayer>() {
            @Override
            public Iterator<ShoutPlayer> iterator() {
                Iterator<? extends Player> iterator = ImmutableList.copyOf(Bukkit.getOnlinePlayers()).iterator();
                return new Iterator<ShoutPlayer>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public ShoutPlayer next() {
                        return player(iterator.next());
                    }
                };
            }

            @Override
            public int size() {
                return Bukkit.getOnlinePlayers().size();
            }
        };
    }

    @Override
    public Collection<ShoutPlayer> players() {
        return this.playerCollection;
    }

    @Override
    public PlatformVersion version() {
        return new PlatformVersion(server.getName(), server.getBukkitVersion());
    }

    @Override
    public ShoutPlayer player(Player platformPlayer) {
        return playerMappings.computeIfAbsent(platformPlayer, k -> new BukkitShoutPlayer(audiences, platformPlayer));
    }

    @Override
    public ShoutCommandInvoker console() {
        return new BukkitShoutCommandInvoker(server.getConsoleSender(), audiences, this);
    }

    @Override
    public ShoutCommandInvoker invoker(CommandSender sender) {
        if (sender instanceof Player) {
            return this.player((Player) sender);
        } else {
            return new BukkitShoutCommandInvoker(server.getConsoleSender(), audiences, this);
        }
    }
}
