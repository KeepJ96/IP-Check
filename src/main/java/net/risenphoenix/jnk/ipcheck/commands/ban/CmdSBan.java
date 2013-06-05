package net.risenphoenix.jnk.ipcheck.commands.ban;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Objects.IPObject;
import net.risenphoenix.jnk.ipcheck.PlayerActions;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdSBan implements IpcCommand{

    @Override
    public void execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission("ipcheck.ban") || sender.isOp()) {
			if (args.length >= 2) {
				PlayerActions ab = new PlayerActions();
				
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
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					// Command Instructions Here
					sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "To ban an IP address, use \'/c ban\'");
				} else {
                                    ArrayList<String> player = new ArrayList<String>();
                                    player.add(args[1]);
                                    
                                    ab.banPlayers(new IPObject("no-ban", player), sender, sb.toString(), true);
				}
			} else {
				sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NUM_ARGS_ERR"));
			}
		} else {
			sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NO_PERM_ERR"));
		}
    }

    @Override
    public String getHelp() {
        return "Bans a single player from your server.";
    }

    @Override
    public String getSyntax() {
        return "sban <player> [message]";
    }

    @Override
    public Permission[] getPermissions() {
        Permission perms[] = {
            new Permission("ipcheck.sban")
        };
        
        return perms;
    }

    @Override
    public String getName() {
        return "Single-Ban";
    }

}
