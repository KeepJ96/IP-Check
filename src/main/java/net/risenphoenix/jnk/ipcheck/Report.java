package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class Report {
	
	private static final String PLUG_NAME = "[IP-Check] ";
	private static final String NO_FIND = "The player or IP specified could not be found. Additionally, there were no similar results.";

	public void execute(ArrayList<String> players, CommandSender sender, String ip, String arg, boolean forPlayer) {
		OfflinePlayer player = null;
		
		if (ip.equals("no-find")) {
			sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + NO_FIND);
			return;
		}
		
		if (forPlayer) {
			player = IPcheck.backend.getPlayer(arg, players);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (String s:players) {
			sb.append(s + ", ");
		}
		
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		
		if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
			sender.sendMessage(ChatColor.GOLD + "Total Accounts found for: " + ip + " ... " + players.size());
		} else {
			sender.sendMessage(ChatColor.GOLD + "Total Accounts found: " + players.size());
		}
		
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		
		if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "The following players connect with the above IP address: " + ChatColor.YELLOW + sb);
		} else {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "The following players connect using the same IP address: " + ChatColor.YELLOW + sb);
		}
		sender.sendMessage("");
		
		if (forPlayer) {
			if (player != null) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "More Information about: " + ChatColor.YELLOW + player.getName());
				if (player.isBanned()) {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Banned: " + ChatColor.RED + "True");
				} else {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Banned: " + ChatColor.GREEN + "False");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "ERROR: " + ChatColor.GOLD + "Player object returned was NULL");
			}
			
			if (Configuration.isExemptPlayer(player.getName())) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Exempt: " + ChatColor.GREEN + "True");
			} else {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Exempt: " + ChatColor.RED + "False");
			}
		}
		
		if (IPcheck.backend.isBannedIP(ip)) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Banned: " + ChatColor.RED + "True");
		} else {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Banned: " + ChatColor.GREEN + "False");
		}
		
		if (Configuration.isExemptIp(ip)) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Exempt: " + ChatColor.GREEN + "True");
		} else {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Exempt: " + ChatColor.RED + "False");
		}
		
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
	}
	
}
