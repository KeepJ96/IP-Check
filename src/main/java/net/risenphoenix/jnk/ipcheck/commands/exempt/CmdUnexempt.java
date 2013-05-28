package net.risenphoenix.jnk.ipcheck.commands.exempt;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.risenphoenix.jnk.ipcheck.configuration.ConfigurationManager;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Language;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdUnexempt implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.exempt.remove") || sender.isOp()) {
			if (args.length == 3) {
				boolean result = IPcheck.Configuration.deleteExemption(args[2]);
				
				if (result) {
					sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.EXEMPTION_DEL_SUC);
				} else {
					sender.sendMessage(ChatColor.GOLD + Language.PLUG_NAME + ChatColor.YELLOW + Language.EXEMPTION_DEL_ERR);
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
		return 5;
	}

	@Override
	public String getHelp() {
		return "Removes the specified exemption from file.";
	}

	@Override
	public String getSyntax() {
		return "exempt remove <player||IP-address>";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.exempt.remove")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Unexempt";
	}

}
