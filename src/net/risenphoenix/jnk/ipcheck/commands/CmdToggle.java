package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdToggle implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.toggle") || sender.isOp()) {
			if (args.length == 2) {
				
				if (args[1].equalsIgnoreCase("login-notify") || args[1].equalsIgnoreCase("notification") || args[1].equalsIgnoreCase("notify")) {
					int response = Configuration.toggle(0);
					
					if (response == 0) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.TOGGLE_NOTIFY + ChatColor.RED + "False");
					} else if (response == 1) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.TOGGLE_NOTIFY + ChatColor.GREEN + "True");
					} else {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.RED + IPcheck.TOGGLE_ERR);
					}
					
				} else if (args[1].equalsIgnoreCase("detail-notify") || args[1].equalsIgnoreCase("detail") || args[1].equalsIgnoreCase("dn")) {
					int response = Configuration.toggle(1);
					
					if (response == 0) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.TOGGLE_DETAIL + ChatColor.RED + "False");
					} else if (response == 1) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.TOGGLE_DETAIL + ChatColor.GREEN + "True");
					} else {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.RED + IPcheck.TOGGLE_ERR);
					}
					
				} else if (args[1].equalsIgnoreCase("secure-mode") || args[1].equalsIgnoreCase("secure")) {
					int response = Configuration.toggle(2);
					
					if (response == 0) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.TOGGLE_SECURE + ChatColor.RED + "False");
					} else if (response == 1) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.TOGGLE_SECURE + ChatColor.GREEN + "True");
					} else {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.RED + IPcheck.TOGGLE_ERR);
					}
					
				} else {
					sender.sendMessage(IPcheck.ILL_ARGS_ERR);
				}
			} else {
				sender.sendMessage(IPcheck.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(IPcheck.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 6;
	}

}
