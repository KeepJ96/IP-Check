package net.risenphoenix.jnk.ipcheck.commands;

import java.io.File;
import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.Essentials;
import net.risenphoenix.jnk.ipcheck.FlatFile;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdConvert implements IpcCommand{

    public void execute(CommandSender sender, String commandLabel, String[] args) {
        ArrayList<String> playerInfo = new ArrayList<String>();
        FlatFile f = new FlatFile();
        boolean convertEssentials = false;
        
        if (args.length > 1) {
            if (args[1].equals("-e")) {
                convertEssentials = true;
            } else {
                sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "Invalid parameter passed!");
                return;
            }
        }
        
        long startTime = System.currentTimeMillis();
        
        if (convertEssentials) {
            Essentials e = new Essentials();
            playerInfo = e.loadFile();
        } else {
            playerInfo = f.loadFile(new File("plugins/IP-check/database.db"));
        }
        
        if (playerInfo == null) {
            sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "Conversion failed!");
            return; // Return because there were no files found to convert from.
        } else {
            for (String s:playerInfo) {
                StringBuilder player = new StringBuilder();
                StringBuilder ip = new StringBuilder();
                
                boolean hasName = false;
                
                // Split the name and IP into separate strings so that we can log them into the new database
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) != '|' && !hasName) { // If we have not hit a '|' yet, then we are scanning the name
                        player.append(s.charAt(i));
                    } else if (s.charAt(i) != '|' && hasName) { // If we have already hit a '|', then we are scanning the IP
                        ip.append(s.charAt(i));
                    } else if (s.charAt(i) == '|') { // If we hit a '|', then we have finished scanning the name.
                        hasName = true;
                    }
                }
                
                f.log(player.toString(), ip.toString()); // Log the information to the new Database
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "Conversion complete! Time taken: " + ((endTime - startTime) / 1000) + " seconds. " +
            "Total number of entries converted: " + playerInfo.size() + ".");
    }
    
    

    public int getID() {
        return 13;
    }

    public String getHelp() {
        return "Converts old database formats into a new database.";
    }

    public String getSyntax() {
        return "convert";
    }

    public Permission[] getPermissions() {
        Permission perms[] = {
            new Permission("ipcheck.convert")
        };

        return perms;
    }

    public String getName() {
        return "Convert";
    }

}
