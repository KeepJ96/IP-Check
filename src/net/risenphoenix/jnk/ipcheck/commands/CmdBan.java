package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.ActionBan;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Report;

import org.bukkit.command.CommandSender;

public class CmdBan implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.ban") || sender.isOp()) {
			if (args.length == 2) {
				Report report = new Report();
				ActionBan ab = new ActionBan();
				
				String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					// Command Instructions Here
					report.execute(ab.banPlayers(IPcheck.backend.getAlts(args[1]), sender, IPcheck.backend.checkIPaddress(args[1]), true), sender, IPcheck.backend.getIP(args[1]), args[1], false);
				} else {
					report.execute(ab.banPlayers(IPcheck.backend.getAlts(IPcheck.backend.getIP(args[1])), sender, IPcheck.backend.getIP(args[1]), true), sender, IPcheck.backend.getIP(args[1]), args[1], false);
				}
			} else {
				sender.sendMessage(IPcheck.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(IPcheck.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 1;
	}

}
