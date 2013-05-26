package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class Report {
	
    private static final String PLUG_NAME = "[IP-Check] ";
    private static final String NO_FIND = "The player or IP specified could not be found.";

    public void execute(CommandSender sender, String arg) {
        ArrayList<String> IPs; // For use with player check
        ArrayList<StringBuilder> SBs = new ArrayList<StringBuilder>(); // For use with player check

        ArrayList<String> singleAlts = new ArrayList<String>(); // For use with IP check
        
        OfflinePlayer player = null;

        boolean forPlayer = true;

        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        //Command Instructions here
        if (arg.toLowerCase().matches(ip_filter.toLowerCase())) {
            forPlayer = false;
        }

        if (forPlayer) {
            player = Bukkit.getOfflinePlayer(arg);
        }
        
        // Get all alt accounts linked to the player
        if (forPlayer) {
            IPs = IPcheck.backend.getIPs(arg); // Load list of IPs from backend

            if (IPs == null) {
                sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + NO_FIND);
                return;
            }
            
            for (String s:IPs) {
                ArrayList<String> altAccounts;

                // Get alt accounts for the IP Address
                if (s.contains("-lastknown")) {
                    altAccounts = IPcheck.backend.getAlts(s.replace("-lastknown", ""));
                } else {
                    altAccounts = IPcheck.backend.getAlts(s);
                }

                // Create New String Builder
                StringBuilder sb = new StringBuilder();

                // Append the leading IP-Address, plus a splitter character '|'
                sb.append(s + "|");

                // Append each account, plus punctuation to the string builder
                for (String account:altAccounts) {
                    sb.append(account + ", ");
                }

                // Add the string builder to the holder arraylist
                SBs.add(sb); 
            }
        } else {
            singleAlts = IPcheck.backend.getAlts(arg);
            
            if (singleAlts == null) {
                sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + NO_FIND);
                return;
            }
        }

        //### OUTPUT MESSAGE ###//
        //### HEADER ###//
        sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
        if (!forPlayer) {
            if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                sender.sendMessage(ChatColor.GOLD + "Total Accounts found for: " + arg + " ... " + singleAlts.size());
            } else {
                sender.sendMessage(ChatColor.GOLD + "Total Accounts found: " + singleAlts.size());
            }
        } else if (forPlayer) {
            int accounts = 0;
            for (StringBuilder sb:SBs) {
                String string = sb.toString();

                for (int i = 0; i < string.length(); i++) {
                    if (string.charAt(i) == ',') {
                        accounts++;
                    }
                }
            }

            sender.sendMessage(ChatColor.GOLD + "Total Accounts found for: " + arg + " ... " + accounts);
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
        //### END HEADER ###//

        //### BODY ###//
        if (!forPlayer) {
            StringBuilder sb = new StringBuilder();

            for (String string:singleAlts) {
                sb.append(string + ", ");
            }

            if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "The following players connect with the above IP address: " + ChatColor.YELLOW + sb);
            } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "The following players connect using the same IP address: " + ChatColor.YELLOW + sb);
            }
            sender.sendMessage("");
        } else if (forPlayer) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Players and IPs associated with the search term: ");

            if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                for (StringBuilder sb:SBs) {
                    String full = sb.toString();
                    StringBuilder ipAddress = new StringBuilder();

                    for (int i = 0; i < full.length(); i ++) {
                        if (full.charAt(i) != '|') {
                            ipAddress.append(full.charAt(i));
                        } else {
                            break;
                        }
                    }

                    full = full.replace(ipAddress.toString() + "|", "");

                    String ipAdd = ipAddress.toString();
                    
                    if (ipAdd.contains("-lastknown")) {
                        ipAdd = ipAdd.replace("-lastknown", " -L");
                    }
                    
                    sender.sendMessage(ChatColor.RED + ipAdd + ":");
                    sender.sendMessage(ChatColor.YELLOW + full);

                    sender.sendMessage("");
                }    
            } else {
                for (StringBuilder sb:SBs) {
                    String full = sb.toString();
                    StringBuilder ipAddress = new StringBuilder();

                    for (int i = 0; i < full.length(); i ++) {
                        if (full.charAt(i) != '|') {
                            ipAddress.append(full.charAt(i));
                        } else {
                            break;
                        }
                    }

                    full = full.replace(ipAddress.toString() + "|", "");

                    sender.sendMessage(ChatColor.RED + "####:");
                    sender.sendMessage(ChatColor.YELLOW + full);

                    sender.sendMessage("");
                }    
            }
        }
            
        sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");

        //### END BODY ###//

        //### FOOTER ###//

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

        if (!forPlayer) {
            if (IPcheck.backend.isBannedIP(arg)) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Banned: " + ChatColor.RED + "True");
            } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Banned: " + ChatColor.GREEN + "False");
            }

            if (Configuration.isExemptIp(arg)) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Exempt: " + ChatColor.GREEN + "True");
            } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "IP Exempt: " + ChatColor.RED + "False");
            }
        }

        if (forPlayer) {
            if (sender.hasPermission("ipcheck.showbanreason")) {
                if (player != null) {
                    if (player.isBanned()) {
                        sender.sendMessage("");
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Banned Reason: " + ChatColor.YELLOW + Configuration.getBannedReason(player.getName()));
                    }
                }
            }
        }
        ///### END FOOTER ###//

        sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");

        //### END OUTPUT MESSAGE ###//
	
    }	
}
