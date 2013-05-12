package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionBan {
	public ArrayList<String> banPlayers(ArrayList<String> players, CommandSender sender, String ip, String message, boolean banning) {
		// Ban or Unban IP Address
		if (ip.equals("no-find")) {
			sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.NO_FIND);
			return players;
		}
		
		if (banning) {
			Bukkit.banIP(ip);
			sender.sendMessage("");
			sender.sendMessage("Banned IP Address: " + ip);
			sender.sendMessage("");
		} else if (!banning) {
			Bukkit.unbanIP(ip);
			sender.sendMessage("");
			sender.sendMessage("Unbanned IP Address: " + ip);
			sender.sendMessage("");
		}
		
		// Ban or Unban Players with corresponding IP Address
		for(String s:players) {
			Bukkit.getOfflinePlayer(s).setBanned(banning);
			
			if (banning) {
				Player player = Bukkit.getPlayer(s);
				
				if (player != null) {
					if (message.length() > 0) {
						player.kickPlayer(message);
					} else {
						player.kickPlayer(Configuration.banMessage);
					}
				}
				
				Player[] online = Bukkit.getOnlinePlayers();
				for (int i = 0; i < online.length; i++) {
					if (message.length() > 0) {
						online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + message);
					} else {
						online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + Configuration.banMessage);
					}
				}
			} else if (!banning) {
				sender.sendMessage("Pardoned " + s);
			}
		}
		
		return players;
	}
}
