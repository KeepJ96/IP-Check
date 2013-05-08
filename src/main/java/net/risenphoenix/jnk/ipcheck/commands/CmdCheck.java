package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Report;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdCheck implements IpcCommand{
	
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		Report report = new Report();
		
		String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		//Command Instructions here
		if (args[0].toLowerCase().matches(ip_filter.toLowerCase())) {
			report.execute(IPcheck.backend.getAlts(args[0]), sender, IPcheck.backend.getIP(args[0]), args[0], false);
		} else {
			report.execute(IPcheck.backend.getAlts(IPcheck.backend.getIP(args[0])), sender, IPcheck.backend.getIP(args[0]), args[0], true);
		}
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public String getHelp() {
		return "Displays information about the player or IP Specified.";
	}

	@Override
	public String getSyntax() {
		return "<Player||IP-Address>";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.use")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Check";
	}

}
