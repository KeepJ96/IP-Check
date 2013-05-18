package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.Listeners.PlayerCommandListener;
import net.risenphoenix.jnk.ipcheck.Listeners.PlayerJoinListener;
import net.risenphoenix.jnk.ipcheck.Listeners.PlayerLoginListener;
import net.risenphoenix.jnk.ipcheck.Logging.DateStamp;
import net.risenphoenix.jnk.ipcheck.Logging.ErrorLogger;
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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class IPcheck extends JavaPlugin implements Listener{
	
	//================== IP-Check v1.2.9 | May 18, 2013 - JNK1296-PC | Author: Jacob Keep (Jnk1296) ==================//
	
	//=================Root Command==================//
	public static final String ROOT_COMMAND = "c";
	public static final String VER_STRING = "IP-Check v1.2.9";
	public static final String COMP_DATE = "May 18, 2013";
	
	//=============== Backend Manager ===============//
	public static Backend backend = null;
	
	//=============== Event Listeners ===============//
	public static PlayerLoginListener PLL = new PlayerLoginListener();
	public static PlayerJoinListener PJL = new PlayerJoinListener();
	public static PlayerCommandListener PCL = new PlayerCommandListener();
	
	//================== Commands ==================//
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
	
	//=============== Global Messages ===============//
	public static final String PLUG_NAME = "[IP-Check] ";
	public static final String BAN_LIST_READ_ERR = "Error occurred while attempting to read banned-ips.txt!";
	public static final String PLAYER_FILE_READ_ERR = "Error occurred while attempting to read player file!";
	public static final String NO_PERM_ERR = "You don't have permission to do that!";
	public static final String NUM_ARGS_ERR = "Incorrect Number of Arguments.";
	public static final String ILL_ARGS_ERR = "Illegal Argument(s) were passed into the command.";
	public static final String NO_FIND = "The player specified could not be found.";
	public static final String NO_RECENT = "You have not searched a player yet.";
	public static final String PLAYER_EXEMPT_SUC = "Player added to exemption list!";
	public static final String IP_EXEMPT_SUC = "IP-Address added to exemption list!";
	public static final String EXEMPTION_FAIL = "Sorry. :( Something went wrong. The exemption could not be added.";
	public static final String TOGGLE_SECURE = "Secure-Mode set to: ";
	public static final String TOGGLE_NOTIFY = "Notify-On-Login set to: ";
	public static final String TOGGLE_DETAIL = "Descriptive-Notify set to: ";
	public static final String TOGGLE_ERR = "An error occurred while attempting to set state of toggle.";
	public static final String EXEMPTION_DEL_SUC = "Exemption successfully removed!";
	public static final String EXEMPTION_DEL_ERR = "Exemption specified does not exist.";
	public static final String ERROR_LOG_RMDR = "An error occurred! A log summary of this error has been saved to IP-Check's directory under ''Error_Reports''";
	
	//============== Global Variables ==============//
	public static boolean shouldCheck = true;
	public static boolean isUsingMineBans = false;
	public static String ipToCheck = "";
	
	//================== Methods ==================//
	
	// Called when plugin is enabled
	@Override
	public void onEnable() { 
		getServer().getPluginManager().registerEvents(this, this); // Register the Player Login Listener
		
		DateStamp ds = new DateStamp();
		String random = Functions.getSeasonalMessage(ds.getCustomStamp("MM-dd")); // Is there an overriding message coded for this date?
		
		if (random != null) {
			Bukkit.getLogger().info(random);
		} else {
			Bukkit.getLogger().info(PLUG_NAME + Functions.getRandomMessage()); // A Nice random Message
		}
		
		Configuration.onLoad();      // Load the Configuration File
		
		// Determine which backend to use
		if (Configuration.backend == 1) {
			backend = new FlatFile();
		} else {
			backend = new Essentials();
		}
		
		backend.onLoad();            // Initialize Backend
		registerCommands();          // Register Commands
		
		
	}
	
	// Called when plugin is disabled
	@Override
	public void onDisable() {
		backend.onDisable();
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
		
		Bukkit.getLogger().info(PLUG_NAME + "Registered " + commands.size() + " commands.");
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
	
	// Event Handler for PlayerCommandEvents
	//@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		PCL.execute(e);
	}
	
	// Called when a command is entered
        @Override
	public boolean onCommand(CommandSender sender, Command root, String commandLabel, String[] args) {
		if (root.getName().equalsIgnoreCase(ROOT_COMMAND)) {
			if (sender.hasPermission("ipcheck.use") || sender.isOp()) {
				int commandID = ParseCommand.execute(args);
				
				if (commandID == -1) {
					sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + "An invalid command was specified.");
					return true;
				}
				
				if (commandID == -2) {
					sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + "An invalid sub-command or no sub-command was specified.");
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
                                                sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + "An error occured while attempting to perform the command." + 
                                                        " Please check IP-Check's plugin folder in your servers plugin directory for a report of this error.");
                                            } finally {
                                                return true;
                                            }
					}
				}
				
			} else {
				sender.sendMessage(NO_PERM_ERR);
				return true;
			}
		}
		
		return false;
	}
}
