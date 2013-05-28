package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.Report;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdCheck implements IpcCommand{
	
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		Report report = new Report();
                report.execute(sender, args[0]);
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
