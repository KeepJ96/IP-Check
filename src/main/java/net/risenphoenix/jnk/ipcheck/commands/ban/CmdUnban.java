package net.risenphoenix.jnk.ipcheck.commands.ban;

import net.risenphoenix.jnk.ipcheck.PlayerActions;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.reports.Report;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdUnban implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.unban") || sender.isOp()) {
			if (args.length == 2) {
				Report report = new Report();
				PlayerActions ab = new PlayerActions();
				
				//Command Instructions Here
				String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					// Command Instructions Here
					ab.banPlayers(IPcheck.Instance.Database.getAlts(args[1]), sender, IPcheck.Instance.Database.checkIPaddress(args[1]), "", false);
				} else {
					ab.banPlayers(IPcheck.Instance.Database.getAlts(IPcheck.Instance.Database.getLastKnownIP(args[1])), sender, IPcheck.Instance.Database.getLastKnownIP(args[1]), "", false);
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
		return "Unbans the Player or IP specified. Additionally, unbans any associated accounts, plus the IP-address.";
	}

	@Override
	public String getSyntax() {
		return "unban <Player||IP-address>";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.unban")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Unban";
	}

}
