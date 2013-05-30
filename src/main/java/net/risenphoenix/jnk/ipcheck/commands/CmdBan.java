package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.ActionBan;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Language;
import net.risenphoenix.jnk.ipcheck.Report;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdBan implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.ban") || sender.isOp()) {
			if (args.length >= 2) {
				Report report = new Report();
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
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					// Command Instructions Here
					ab.banPlayers(IPcheck.Database.getAlts(args[1]), sender, IPcheck.Database.checkIPaddress(args[1]), sb.toString(), true);
				} else {
					ab.banPlayers(IPcheck.Database.getAlts(IPcheck.Database.getLastKnownIP(args[1])), sender, IPcheck.Database.getLastKnownIP(args[1]), sb.toString(), true);
				}
			} else {
				sender.sendMessage(Language.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(Language.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 1;
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
