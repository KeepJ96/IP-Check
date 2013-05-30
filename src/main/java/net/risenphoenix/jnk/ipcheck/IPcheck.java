package net.risenphoenix.jnk.ipcheck;

import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.CommandManager;
import net.risenphoenix.jnk.ipcheck.configuration.ConfigurationManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.listeners.PlayerJoinListener;
import net.risenphoenix.jnk.ipcheck.listeners.PlayerLoginListener;
import net.risenphoenix.jnk.ipcheck.logging.DateStamp;
import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;
import net.risenphoenix.jnk.ipcheck.commands.CmdAbout;
import net.risenphoenix.jnk.ipcheck.commands.ban.CmdBan;
import net.risenphoenix.jnk.ipcheck.commands.CmdCheck;
import net.risenphoenix.jnk.ipcheck.commands.CmdConvert;
import net.risenphoenix.jnk.ipcheck.commands.CmdHelp;
import net.risenphoenix.jnk.ipcheck.commands.CmdKick;
import net.risenphoenix.jnk.ipcheck.commands.CmdReload;
import net.risenphoenix.jnk.ipcheck.commands.ban.CmdSBan;
import net.risenphoenix.jnk.ipcheck.commands.CmdToggle;
import net.risenphoenix.jnk.ipcheck.commands.ban.CmdUnban;
import net.risenphoenix.jnk.ipcheck.commands.CmdPurge;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdExempt;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdUnexempt;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExemptListAll;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExemptListIp;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExemptListPlayer;
import net.risenphoenix.jnk.ipcheck.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * @version 1.3.1
 * @author Jacob Keep (Jnk1296)
 */
public class IPcheck extends JavaPlugin implements Listener{
    public static IPcheck Instance;	
    //Root Command
    public static final String ROOT_COMMAND = "c";
    public static String VER_STRING;
    public static Date COMP_DATE;
    
    //Configuration
    public static ConfigurationManager Configuration;

    //Database
    public static DatabaseManager Database;
    
    //Commands
    public static CommandManager Commands;

    //Event Listeners
    public static PlayerLoginListener PLL = new PlayerLoginListener();
    public static PlayerJoinListener PJL = new PlayerJoinListener();


    //Methods
    // Called when plugin is enabled
    @Override
    public void onEnable() { 
        this.saveDefaultConfig(); // Create config if there is none
        Instance=this;
        
        Configuration = new ConfigurationManager(); 
        Database = new DatabaseManager(getConfig().getBoolean("use-mysql")); 
        Commands = new CommandManager();
        
        VER_STRING = this.getDescription().getVersion(); // Getting Version from plugin.yml 
        try {
            COMP_DATE = new Date(IPcheck.class.getResource("IPcheck.class").openConnection().getLastModified()); //Getting compiletime from class file
        } catch (IOException ex) {
            Logger.getLogger(IPcheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        getServer().getPluginManager().registerEvents(this, this); // Register the Player Login Listener

        DateStamp ds = new DateStamp();
        String random = RandomMessages.getSeasonalMessage(ds.getCustomStamp("MM-dd")); // Is there an overriding message coded for this date?

        if (random != null) {
            Bukkit.getLogger().info(random);
        } else {
            Bukkit.getLogger().info(TranslationManager.PLUG_NAME + RandomMessages.getRandomMessage()); // A Nice random Message
        }
    }

    // Called when plugin is disabled
    @Override
    public void onDisable() {
        Database.close();
    }
    public static JavaPlugin getInstance(){
        return Instance;
    }

    // Event Handler for PlayerLoginEvents
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent e) {
        PLL.execute(e);
    }

    // Event Handler for PlayerJoinEvents
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        PJL.execute(e);
    }

    // Called when a command is entered
    @Override
    public boolean onCommand(CommandSender sender, Command root, String commandLabel, String[] args) {
        if (root.getName().equalsIgnoreCase(ROOT_COMMAND)) {
            if (sender.hasPermission("ipcheck.use") || sender.isOp()) {
                IpcCommand command = Commands.executeCommand(args,sender);

                if (command == null) {
                    return false;
                }
                
                try {
                    command.execute(sender, commandLabel, args); // Execute
                    return true;
                } catch (Exception e) {
                    ErrorLogger EL = new ErrorLogger();
                    EL.execute(e);
                    sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.ERROR_LOG_RMDR);
                } finally {
                    return true;
                }
            } else {
                sender.sendMessage(TranslationManager.NO_PERM_ERR);
                return true;
            }
        }

        return false;
    }
}
