package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Language;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdToggle implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.toggle") || sender.isOp()) {
			if (args.length == 2) {
				
				if (args[1].equalsIgnoreCase("login-notify") || args[1].equalsIgnoreCase("notification") || args[1].equalsIgnoreCase("notify")) {
					int response = IPcheck.Configuration.toggle(0);
					
					if (response == 0) {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.TOGGLE_NOTIFY + ChatColor.RED + "False");
					} else if (response == 1) {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.TOGGLE_NOTIFY + ChatColor.GREEN + "True");
					} else {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.RED + Language.TOGGLE_ERR);
					}
					
				} else if (args[1].equalsIgnoreCase("detail-notify") || args[1].equalsIgnoreCase("detail") || args[1].equalsIgnoreCase("dn")) {
					int response = IPcheck.Configuration.toggle(1);
					
					if (response == 0) {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.TOGGLE_DETAIL + ChatColor.RED + "False");
					} else if (response == 1) {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.TOGGLE_DETAIL + ChatColor.GREEN + "True");
					} else {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.RED + Language.TOGGLE_ERR);
					}
					
				} else if (args[1].equalsIgnoreCase("secure-mode") || args[1].equalsIgnoreCase("secure") || args[1].equalsIgnoreCase("sm")) {
					int response = IPcheck.Configuration.toggle(2);
					
					if (response == 0) {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.TOGGLE_SECURE + ChatColor.RED + "False");
					} else if (response == 1) {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.TOGGLE_SECURE + ChatColor.GREEN + "True");
					} else {
						sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.RED + Language.TOGGLE_ERR);
					}
					
				} else if (args[1].equalsIgnoreCase("help")) {
					
					sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + "List of Toggle Options:");
					sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
					sender.sendMessage(ChatColor.RED + "  Login-Notifications:" + ChatColor.YELLOW + " <" + ChatColor.LIGHT_PURPLE + " notify " + ChatColor.YELLOW + "|" + ChatColor.LIGHT_PURPLE + " notification " + ChatColor.YELLOW + "|" + ChatColor.LIGHT_PURPLE + " login-notify" + ChatColor.YELLOW + " >");
					sender.sendMessage(ChatColor.RED + "  Detailed-Notifications:" + ChatColor.YELLOW + " <" + ChatColor.LIGHT_PURPLE + " detail " + ChatColor.YELLOW + "|" + ChatColor.LIGHT_PURPLE + " detail-notify " + ChatColor.YELLOW + "|" + ChatColor.LIGHT_PURPLE + " dn " + ChatColor.YELLOW + ">");
					sender.sendMessage(ChatColor.RED + "  Secure-Mode:" + ChatColor.YELLOW + " <" + ChatColor.LIGHT_PURPLE + " secure-mode " + ChatColor.YELLOW + "|" + ChatColor.LIGHT_PURPLE + " secure " + ChatColor.YELLOW + "|" + ChatColor.LIGHT_PURPLE + " sm " + ChatColor.YELLOW + ">");
					sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
					
				} else {
					sender.sendMessage(Language.ILL_ARGS_ERR);
				}
			} else {
				sender.sendMessage(Language.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(Language.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 6;
	}

	@Override
	public String getHelp() {
		return "Toggles the specified option. For a list of options, type ''/c toggle help''";
	}

	@Override
	public String getSyntax() {
		return "toggle <option>";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.toggle")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Toggle";
	}

}
