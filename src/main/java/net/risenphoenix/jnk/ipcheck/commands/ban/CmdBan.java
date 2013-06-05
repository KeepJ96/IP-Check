package net.risenphoenix.jnk.ipcheck.commands.ban;

import net.risenphoenix.jnk.ipcheck.PlayerActions;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.reports.Report;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdBan implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.ban") || sender.isOp()) {
			if (args.length >= 2) {
				Report report = new Report();
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
					ab.banPlayers(IPcheck.Instance.Database.getAlts(args[1]), sender, sb.toString(), true);
				} else {
					ab.banPlayers(IPcheck.Instance.Database.getAlts(IPcheck.Instance.Database.getLastKnownIP(args[1])), sender, sb.toString(), true);
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
		return "Bans the player or IP specified. In addition, this command will also ban any alternative accounts associated, plus the IP-address.";
	}

	@Override
	public String getSyntax() {
		return "ban <Player||IP-address> [message]";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.ban")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Ban";
	}

}
