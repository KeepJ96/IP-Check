package net.risenphoenix.jnk.ipcheck;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LoginReport {

	private static final String PLUG_NAME = "[IP-Check] ";
	
	public void execute(String ip, Player player, int accounts) {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		Player curPlayer = player;
		
		if ((curPlayer.hasPlayedBefore() && (accounts - 1) > Configuration.notifyThreshold) || (!curPlayer.hasPlayedBefore() && accounts > Configuration.notifyThreshold)) {
			if (!Configuration.isExemptPlayer(player.getName()) && !Configuration.isExemptIp(ip)) { // If number of accounts is greater than threshold and player is not exempt
				
				if (curPlayer.hasPlayedBefore()) {
					accounts -= 1; // subtract one from playersfound to make up for Recursion
				}
				
				for(int i = 0; i < online.length; i++) {
					if (Configuration.detailNotify) { // If Detailed Notifications are enabled, display the detailed notification.
						if (online[i].hasPermission("ipcheck.getnotify") || online[i].isOp()) { // If player is an operator or has the ipcheck.getnotify permission
							online[i].sendMessage(ChatColor.DARK_GRAY + "---------------------------------------");
							online[i].sendMessage(ChatColor.GOLD + "Background Report for: " + ChatColor.LIGHT_PURPLE + player.getDisplayName());
							online[i].sendMessage(ChatColor.DARK_GRAY + "---------------------------------------");
							online[i].sendMessage(ChatColor.GOLD + "IP Address: " + ChatColor.LIGHT_PURPLE + ip);
							online[i].sendMessage(ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.RED + " was found to have " + ChatColor.YELLOW + accounts + ChatColor.RED +
									" possible alternative accounts. Perform command " + ChatColor.LIGHT_PURPLE + "'/c " + player.getDisplayName() + "'" + 
									ChatColor.RED + " for more information.");
							online[i].sendMessage(ChatColor.DARK_GRAY + "---------------------------------------");
						}
					} else if (!Configuration.detailNotify) { // If Detailed Notifications are disabled, display the simple notification.
						if (online[i].hasPermission("ipcheck.getnotify") || online[i].isOp()) { // If player is an operator or has the ipcheck.getnotify permission
							online[i].sendMessage(ChatColor.DARK_GRAY + "---------------------------------------");
							online[i].sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.RED + "Warning! " + ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.RED + " may have multiple accounts!");
							online[i].sendMessage(ChatColor.DARK_GRAY + "---------------------------------------");
						}
					}
				}
			}
		}
	}
	
}
