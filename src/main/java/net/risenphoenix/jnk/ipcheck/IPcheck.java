package net.risenphoenix.jnk.ipcheck;

import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.CommandManager;
import net.risenphoenix.jnk.ipcheck.configuration.ConfigurationManager;
import net.risenphoenix.jnk.ipcheck.listeners.PlayerJoinListener;
import net.risenphoenix.jnk.ipcheck.listeners.PlayerLoginListener;
import net.risenphoenix.jnk.ipcheck.logging.DateStamp;
import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
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
    
    public final static String PLUG_NAME = "[IP-Check] ";
    public final static String ROOT_COMMAND = "c";
    
    //Configuration
    public ConfigurationManager Configuration;

    //Database
    public DatabaseManager Database;
    
    //Commands
    public CommandManager Commands;
    
    //Commands
    public TranslationManager Translation;
    

    //Event Listeners
    public static PlayerLoginListener PLL = new PlayerLoginListener();
    public static PlayerJoinListener PJL = new PlayerJoinListener();


    //Methods
    // Called when plugin is enabled
    @Override
    public void onEnable() { 
        saveDefaultConfig(); // Create config if there is none
        this.Instance=this;
        Configuration = new ConfigurationManager(); 
        Translation = new TranslationManager(getConfig().getString("language"));
        Database = new DatabaseManager(getConfig().getBoolean("use-mysql")); 
        Commands = new CommandManager();
        
        getServer().getPluginManager().registerEvents(this, this); // Register the Player Login Listener
        showRandomMessage();
    }
    
    private void showRandomMessage(){
        DateStamp ds = new DateStamp();
        String random = RandomMessages.getSeasonalMessage(ds.getCustomStamp("MM-dd")); // Is there an overriding message coded for this date?
        if (random != null) {
            Bukkit.getLogger().info(random);
        } else {
            Bukkit.getLogger().info(PLUG_NAME + RandomMessages.getRandomMessage()); // A Nice random Message
        }
    }

    // Called when plugin is disabled
    @Override
    public void onDisable() {
        Database.close();
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
                    sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + Translation.getTranslation("ERROR_LOG_RMDR"));
                } finally {
                    return true;
                }
            } else {
                sender.sendMessage(Translation.getTranslation("NO_PERM_ERR"));
                return true;
            }
        }

        return false;
    }
}
