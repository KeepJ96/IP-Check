package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.ActionBan;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdKick implements IpcCommand{

    @Override
    public void execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission("ipcheck.kick") || sender.isOp()) {
			if (args.length >= 2) {
				ActionBan ab = new ActionBan();
				
				// Create the ban Message, if one exists.
				StringBuilder sb = new StringBuilder();
				if (args.length > 2) {
					for (int argsPos = 2; argsPos < args.length; argsPos++) {
						sb.append(args[argsPos]);
						if (!(argsPos == (args.length - 1))) {
							sb.append(" ");
						} else {
							sb.append(".");
						}
					}
				}
				
				String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
                                int total;
                                
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					// Command Instructions here
                                    total = ab.kickPlayers(IPcheck.backend.getAlts(args[1]), sender, IPcheck.backend.checkIPaddress(args[1]), sb.toString());
				} else {
                                    total = ab.kickPlayers(IPcheck.backend.getAlts(IPcheck.backend.getIP(args[1])), sender, IPcheck.backend.getIP(args[1]), sb.toString());
				}
                                
                                sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "Kicked " + total + " players.");
			} else {
				sender.sendMessage(IPcheck.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(IPcheck.NO_PERM_ERR);
		}
    }

    @Override
    public int getID() {
        return 14;
    }

    @Override
    public String getHelp() {
        return "Kicks all players linked to player or IP specified.";
    }

    @Override
    public String getSyntax() {
        return "kick <Player||IP-address> [message]";
    }

    @Override
    public Permission[] getPermissions() {
        Permission perms[] = {
            new Permission("ipcheck.kick")
        };
		
        return perms;
    }

    @Override
    public String getName() {
        return "Kick";
    }

}
