package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdPurge implements IpcCommand{

    public void execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission("ipcheck.purge") || sender.isOp()) {
            if (args.length > 1 && args.length < 3) {
                String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

                // DELETING AN IP ENTRY
                if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
                  if(IPcheck.Instance.Database.purgeIP(args[1])){
                        sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + String.format(IPcheck.Instance.Translation.getTranslation("PURGE_SUC"),args[1]));
                  }else{
                      sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + String.format(IPcheck.Instance.Translation.getTranslation("PURGE_ERR"),args[1]));
                  }
                  
                } else{
                    if(IPcheck.Instance.Database.purgePlayer(args[1])){
                        sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + String.format(IPcheck.Instance.Translation.getTranslation("PURGE_SUC"),args[1]));
                    }else{
                        sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + String.format(IPcheck.Instance.Translation.getTranslation("PURGE_ERR"),args[1]));
                    } 
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + IPcheck.Instance.Translation.getTranslation("NUM_ARGS_ERR"));
            }
        } else {
            sender.sendMessage(ChatColor.GOLD + IPcheck.Instance.PLUG_NAME + ChatColor.YELLOW + IPcheck.Instance.Translation.getTranslation("NO_PERM_ERR"));
        }
    }

    public int getID() {
        return 160;
    }

    public String getHelp() {
        return "Deletes records of the IP or Player name specified.";
    }

    public String getSyntax() {
        return "purge <Player||IP-Address>";
    }

    public Permission[] getPermissions() {
        Permission[] perms = {
          new Permission("ipcheck.purge")  
        };
        
        return perms;
    }

    public String getName() {
        return "Purge";
    }

}