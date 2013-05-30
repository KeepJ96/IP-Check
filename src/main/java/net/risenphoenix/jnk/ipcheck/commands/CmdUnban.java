package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.ActionBan;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Language;
import net.risenphoenix.jnk.ipcheck.Report;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdUnban implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.unban") || sender.isOp()) {
			if (args.length == 2) {
				Report report = new Report();
				ActionBan ab = new ActionBan();
				
				//Command Instructions Here
				String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					// Command Instructions Here
					ab.banPlayers(IPcheck.Database.getAlts(args[1]), sender, IPcheck.Database.checkIPaddress(args[1]), "", false);
				} else {
					ab.banPlayers(IPcheck.Database.getAlts(IPcheck.Database.getLastKnownIP(args[1])), sender, IPcheck.Database.getLastKnownIP(args[1]), "", false);
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
		return 2;
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
