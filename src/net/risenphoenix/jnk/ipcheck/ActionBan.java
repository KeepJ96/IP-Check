package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionBan {
	public ArrayList<String> banPlayers(ArrayList<String> players, CommandSender sender, String ip, boolean banning) {
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
					player.kickPlayer(Configuration.banMessage);
				}
				
				sender.sendMessage("Banned " + s);
			} else if (!banning) {
				sender.sendMessage("Pardoned " + s);
			}
		}
		
		return players;
	}
}
