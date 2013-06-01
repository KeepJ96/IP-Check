package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdReload implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.reload") || sender.isOp()) {
			if (args.length == 1) {
				IPcheck.Instance.Configuration.initialize();
                                IPcheck.Instance.Translation.reloadTranslation();
			} else {
				sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NUM_ARGS_ERR"));
			}
		} else {
			sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NO_PERM_ERR"));
		}
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
