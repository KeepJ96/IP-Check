package net.risenphoenix.jnk.ipcheck;

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
import net.risenphoenix.jnk.ipcheck.commands.CmdBan;
import net.risenphoenix.jnk.ipcheck.commands.CmdCheck;
import net.risenphoenix.jnk.ipcheck.commands.CmdConvert;
import net.risenphoenix.jnk.ipcheck.commands.CmdHelp;
import net.risenphoenix.jnk.ipcheck.commands.CmdKick;
import net.risenphoenix.jnk.ipcheck.commands.CmdReload;
import net.risenphoenix.jnk.ipcheck.commands.CmdSBan;
import net.risenphoenix.jnk.ipcheck.commands.CmdToggle;
import net.risenphoenix.jnk.ipcheck.commands.CmdUnban;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import net.risenphoenix.jnk.ipcheck.commands.ParseCommand;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdExemptIp;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdExemptPlayer;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdUnexempt;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExmtListAll;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExmtListIp;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExmtListPlayer;
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
    
    public static ConfigurationManager Configuration;

    //Backend Manager
    public static DatabaseManager Database;

    //Event Listeners
    public static PlayerLoginListener PLL = new PlayerLoginListener();
    public static PlayerJoinListener PJL = new PlayerJoinListener();

    //Commands
    public static ArrayList<IpcCommand> commands = new ArrayList<IpcCommand>();
    public static final IpcCommand check = new CmdCheck();
    public static final IpcCommand ban = new CmdBan();
    public static final IpcCommand unban = new CmdUnban();
    public static final IpcCommand exemptIp = new CmdExemptIp();
    public static final IpcCommand exemptPlayer = new CmdExemptPlayer();
    public static final IpcCommand unexempt = new CmdUnexempt();
    public static final IpcCommand toggle = new CmdToggle();
    public static final IpcCommand exemptList_all = new CmdExmtListAll();
    public static final IpcCommand exemptList_ip = new CmdExmtListIp();
    public static final IpcCommand exemptList_player = new CmdExmtListPlayer();
    public static final IpcCommand reload = new CmdReload();
    public static final IpcCommand about = new CmdAbout();
    public static final IpcCommand help = new CmdHelp();
    public static final IpcCommand convert = new CmdConvert();
    public static final IpcCommand kick = new CmdKick();
    public static final IpcCommand sban = new CmdSBan();

    //Methods
    // Called when plugin is enabled
    @Override
    public void onEnable() { 
        this.saveDefaultConfig(); // Create config if there is none
        Instance=this;
        
        Configuration = new ConfigurationManager(); 
        
        VER_STRING = this.getDescription().getVersion(); // Getting Version from plugin.yml 
        try {
            COMP_DATE = new Date(IPcheck.class.getResource("IPcheck.class").openConnection().getLastModified()); //Getting compiletime from class file
        } catch (IOException ex) {
            Logger.getLogger(IPcheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        getServer().getPluginManager().registerEvents(this, this); // Register the Player Login Listener

        DateStamp ds = new DateStamp();
        String random = Functions.getSeasonalMessage(ds.getCustomStamp("MM-dd")); // Is there an overriding message coded for this date?

        if (random != null) {
            Bukkit.getLogger().info(random);
        } else {
            Bukkit.getLogger().info(Language.PLUG_NAME + Functions.getRandomMessage()); // A Nice random Message
        }

        Database = new DatabaseManager(getConfig().getBoolean("use-mysql")); 
        registerCommands();          // Register Commands
    }

    // Called when plugin is disabled
    @Override
    public void onDisable() {
        Database.close();
    }
    public static JavaPlugin getInstance(){
        return Instance;
    }

    // Registers all existing commands with the global arraylist
    public void registerCommands() {
        //----- Command Register -----||----- Command Description ----- || ----- Command ID -----//
        //=======================================================================================//
        commands.add(check);               //Default Check Command             || 0
        commands.add(ban);                 //Ban Command                       || 1
        commands.add(unban);               //Unban Command                     || 2
        commands.add(exemptIp);            //Exempt Command (IP)               || 3
        commands.add(exemptPlayer);        //Exempt Command (Player)           || 4
        commands.add(unexempt);            //Unexempt Command                  || 5
        commands.add(toggle);              //Toggle Command                    || 6
        commands.add(exemptList_all);      //Exempt-List Command (list)        || 7
        commands.add(exemptList_ip);       //Exempt-List Command (IP)          || 8
        commands.add(exemptList_player);   //Exempt-List Command (Player)      || 9
        commands.add(reload);              //Reload Command                    || 10
        commands.add(about);               //About Command                     || 11
        commands.add(help);                //Help Command                      || 12
        commands.add(convert);             //Convert Command                   || 13
        commands.add(kick);                //Kick Command                      || 14
        commands.add(sban);                //SBan Command                      || 15
        //=======================================================================================//

        Bukkit.getLogger().info(Language.PLUG_NAME + "Registered " + commands.size() + " commands.");
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
                int commandID = ParseCommand.execute(args);

                if (commandID == -1) {
                    sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + "An invalid command was specified.");
                    return true;
                }

                if (commandID == -2) {
                    sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + "An invalid sub-command or no sub-command was specified.");
                    return true;
                }

                if (commandID == -3) {
                    return false;
                }

                for (IpcCommand cmd : commands) {
                    if (cmd.getID() == commandID) {
                        try {
                            cmd.execute(sender, commandLabel, args); // Execute
                            return true;
                        } catch (Exception e) {
                            ErrorLogger EL = new ErrorLogger();
                            EL.execute(e);
                            sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.ERROR_LOG_RMDR);
                        } finally {
                            return true;
                        }
                    }
                }

            } else {
                sender.sendMessage(Language.NO_PERM_ERR);
                return true;
            }
        }

        return false;
    }
}
