package net.risenphoenix.jnk.ipcheck.commands;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.Essentials;
import net.risenphoenix.jnk.ipcheck.FlatFile;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdConvert implements IpcCommand{

    public void execute(CommandSender sender, String commandLabel, String[] args) {
        long startTime = System.currentTimeMillis();
        Essentials e = new Essentials();
        FlatFile f = new FlatFile();
        
        ArrayList<String> playerInfo = e.loadFile();
        
        if (playerInfo == null) {
            sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "An error occurred during the convertion. The process has been halted.");
            return;
        }
        
        f.writeConversionFile(playerInfo);
        
        long endTime = System.currentTimeMillis();
        
        sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "Conversion complete! Time taken: " + ((endTime - startTime) / 1000) + " seconds. " +
                "Total number of entries converted: " + playerInfo.size() + ".");
    }

    public int getID() {
        return 13;
    }

    public String getHelp() {
        return "Converts Essentials data to Flat-File format.";
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
