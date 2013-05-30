package net.risenphoenix.jnk.ipcheck;

import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.configuration.ConfigurationManager;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerActions {
	public void banPlayers(ArrayList<String> players, CommandSender sender, String ip, String message, boolean banning) {
            // Ban or Unban IP Address
            if (ip.equals("no-find")) {
                sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.NO_FIND);
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
                    IPcheck.Database.banPlayer(offPlayer.getName(), message);
                } else {
                    IPcheck.Database.banPlayer(offPlayer.getName(), IPcheck.Configuration.banMessage);
                }
                
                if (banning) {
                    Player player = Bukkit.getPlayer(s);

                    if (player != null) {
                        if (message.length() > 0) {
                            player.kickPlayer(message);
                        } else {
                            player.kickPlayer(IPcheck.Configuration.banMessage);
                        }
                    }

                    Player[] online = Bukkit.getOnlinePlayers();
                    for (int i = 0; i < online.length; i++) {
                        if (online[i].hasPermission("ipcheck.seeban")) {
                            if (message.length() > 0) {
                                online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + message);
                            } else {
                                online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + IPcheck.Configuration.banMessage);
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
                sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.NO_FIND);
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
