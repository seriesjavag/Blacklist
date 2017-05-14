package S3ries.Purgeblacklist.Main.commands;



import S3ries.Purgeblacklist.Main.Main;
import S3ries.Purgeblacklist.Main.utils.UUIDFetcher;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerLoginEvent;

public class BlacklistCommand implements Listener, CommandExecutor
{
    private File blacklistFile;
    
    public BlacklistCommand() {
        this.blacklistFile = new File(Main.instance.getDataFolder(), "blacklist.yml");
    }
    
    public void blackListPlayer(final String string) throws IOException {
        final FileConfiguration data = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(Main.instance.getDataFolder(), "blacklist.yml"));
        data.set(String.valueOf(string), (Object)1);
        data.save(this.blacklistFile);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("blacklist") && p.isOp()) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/blacklist [player]"));
            }
            else if (args.length == 1) {
                final String player = args[0];
                final UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player));
                Map<String, UUID> response = null;
                try {
                    response = fetcher.call();
                    this.blackListPlayer(String.valueOf(response));
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4&l" + sender.getName() + " &chas blacklisted &4&l" + args[0] + " &cfrom the PurgePots network!"));
                    sender.sendMessage("");
                    sender.sendMessage("§4*§c Blacklisted by: §4 " + sender.getName());
                    sender.sendMessage("§4*§c Reason: Blacklisted");
                    sender.sendMessage("§4*§c Blacklisted user has been saved to a database §7(Contant owner for unban)");
                    if (Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[0]).isOnline()) {
                        Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&bYour account is blacklisted from the PurgePots Network this type of punishment cannot be appealed!"));
                    }
                }
                catch (Exception e) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError!"));
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
@EventHandler(priority = EventPriority.HIGHEST)
    public void onLoginBlackListed(final PlayerLoginEvent e) {
        final FileConfiguration data = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(Main.instance.getDataFolder(), "blacklist.yml"));
        final Player p = e.getPlayer();
        final UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(p.getName()));
        Map<String, UUID> response = null;
        try {
            response = fetcher.call();
            if (data.getInt(String.valueOf(response)) == 1) {
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', "&b Your account is blacklisted from the PurgePots Network this type of punishment cannot be appealed!"));
            }
        }
        catch (Exception e2) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cError!"));
            e2.printStackTrace();
        }
    }
}
