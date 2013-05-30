package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Language;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdReload implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.reload") || sender.isOp()) {
			if (args.length == 1) {
				IPcheck.Configuration.initialize();
			} else {
				sender.sendMessage(Language.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(Language.NO_PERM_ERR);
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
