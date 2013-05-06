package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdReload implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.reload") || sender.isOp()) {
			if (args.length == 1) {
				Configuration.onLoad();
				IPcheck.backend.onLoad();
			} else {
				sender.sendMessage(IPcheck.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(IPcheck.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 10;
	}

	@Override
	public String getHelp() {
		return "Reloads the IP-Check plugin.";
	}

	@Override
	public String getSyntax() {
		return "reload";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.reload")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Reload";
	}

}
