package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.Objects.IPObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerActions {
	public void banPlayers(IPObject ipo, CommandSender sender, String message, boolean banning) {
            // Ban or Unban IP Address
            if (ipo.getIP().equals("no-find")) {
                sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + IPcheck.Instance.Translation.getTranslation("NO_FIND"));
                return;
            }

            if (!ipo.getIP().equals("no-ban")) {
                if (banning) {
                    Bukkit.banIP(ipo.getIP());
                    sender.sendMessage("");
                    sender.sendMessage("Banned IP Address: " + ipo.getIP());
                    sender.sendMessage("");
                } else if (!banning) {
                    Bukkit.unbanIP(ipo.getIP());
                    sender.sendMessage("");
                    sender.sendMessage("Unbanned IP Address: " + ipo.getIP());
                    sender.sendMessage("");
                }
            }

            // Ban or Unban Players with corresponding IP Address
            for(String s:ipo.getUsers()) {
                OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(s);

                offPlayer.setBanned(banning);

                if (message.length() > 0) {
                    IPcheck.Instance.Database.banPlayer(offPlayer.getName(), message);
                } else {
                    IPcheck.Instance.Database.banPlayer(offPlayer.getName(), IPcheck.Instance.Configuration.banMessage);
                }
                
                if (banning) {
                    Player player = Bukkit.getPlayer(s);

                    if (player != null) {
                        if (message.length() > 0) {
                            player.kickPlayer(message);
                        } else {
                            player.kickPlayer(IPcheck.Instance.Configuration.banMessage);
                        }
                    }

                    Player[] online = Bukkit.getOnlinePlayers();
                    for (int i = 0; i < online.length; i++) {
                        if (online[i].hasPermission("ipcheck.seeban")) {
                            if (message.length() > 0) {
                                online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + message);
                            } else {
                                online[i].sendMessage(ChatColor.GOLD + "Player " + ChatColor.RED + s + ChatColor.GOLD + " was banned by " + ChatColor.GREEN + sender.getName() + ChatColor.GOLD + " for: " + IPcheck.Instance.Configuration.banMessage);
                            }
                        }
                    }
                } else if (!banning) {
                        sender.sendMessage("Pardoned " + s);
                }
            }
	}
        
        public int kickPlayers(IPObject ipo, CommandSender sender, String ip, String message) {
            
            int playersKicked = 0;
            
            if (ip.equals("no-find")) {
                sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + IPcheck.Instance.Translation.getTranslation("NO_FIND"));
                return -1;
            }
            
            for(String s:ipo.getUsers()) {
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
