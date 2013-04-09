package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class Report {
	
	private static final String PLUG_NAME = "[IP-Check] ";
	private static final String NO_FIND = "The player or IP specified could not be found.";

	public void execute(ArrayList<String> players, CommandSender sender, String ip, String arg, boolean forPlayer) {
		OfflinePlayer player = null;
		
		if (ip.equals("no-find")) {
			sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + NO_FIND);
			return;
		}
		
		if (forPlayer) {
			player = IPcheck.backend.getPlayer(ip);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (String s:players) {
			sb.append(s + ", ");
		}
		
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		sender.sendMessage(ChatColor.GOLD + "Total Accounts found for: " + ip + " ... " + players.size());
		sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "The following players connect with the above IP address: " + ChatColor.YELLOW + sb);
		sender.sendMessage("");
		
		if (forPlayer) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Searched for: " + ChatColor.YELLOW + arg);
			if (player.isBanned()) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Banned: " + ChatColor.RED + "True");
			} else {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "Player Banned: " + ChatColor.GREEN + "False");
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
