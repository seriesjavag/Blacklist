package S3ries.Purgeblacklist.Main;

import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import S3ries.Purgeblacklist.Main.commands.BlacklistCommand;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin implements Listener
{
    public static Main instance;
    public static Plugin pl;
    PluginManager manager;
    
    public Main() {
        this.manager = Bukkit.getServer().getPluginManager();
    }
    
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.BOLD + "----------------------------------");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BOLD + "    Blacklist by S3ries has been enabled");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BOLD + "         Enabled version (iHCF)      ");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BOLD + "----------------------------------");
        Main.instance = this;
        this.getCommand("blacklist").setExecutor((CommandExecutor)new BlacklistCommand());
        this.getServer().getPluginManager().registerEvents((Listener)new BlacklistCommand(), (Plugin)this);
       
        final File file2 = new File(new File(Main.instance.getDataFolder(), "data"), "blacklist.yml");
        if (!file2.exists()) {
            this.createBlackListFile();
        }
    }  
    
    public void createBlackListFile() {
        try {
            final FileConfiguration black = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(Main.instance.getDataFolder(), "blacklist.yml"));
            final File blacklistFile = new File(Main.instance.getDataFolder(), "blacklist.yml");
            black.save(blacklistFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Main getInstance() {
        return Main.instance;
    }
}



        