package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionBan {
	public void banPlayers(ArrayList<String> players, CommandSender sender, String ip, String message, boolean banning) {
            // Ban or Unban IP Address
            if (ip.equals("no-find")) {
                sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.NO_FIND);
                return;
            }

            if (!ip.equals("no-ban")) {
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
            }

            // Ban or Unban Players with corresponding IP Address
            for(String s:players) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(s);

                offPlayer.setBanned(banning);

                if (message.length() > 0) {
                    Configuration.writeBannedEntry(offPlayer.getName(), message);
                } else {
                    Configuration.writeBannedEntry(offPlayer.getName(), Configuration.banMessage);
                }

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
                        if (online[i].hasPermission("ipcheck.seeban")) {
                            if (message.length() > 0) {
                                online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + message);
                            } else {
                                online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + Configuration.banMessage);
                            }
                        }
                    }
                } else if (!banning) {
                        sender.sendMessage("Pardoned " + s);
                }
            }
	}
        
        public int kickPlayers(ArrayList<String> players, CommandSender sender, String ip, String message) {
            
            int playersKicked = 0;
            
            if (ip.equals("no-find")) {
                sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.NO_FIND);
                return -1;
            }
            
            for(String s:players) {
                Player player = Bukkit.getPlayer(s);
                                
                if (player != null) {
                    if (message.length() > 0) {
                        player.kickPlayer(message);
                    } else {
                        player.kickPlayer("Kicked from server.");
                    }
                    
                    playersKicked++;
                }
            }
            
            return playersKicked;
        }
}
