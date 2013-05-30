package net.risenphoenix.jnk.ipcheck.commands.exempt;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdExempt implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.exempt") || sender.isOp()) {
			if (args.length == 2) {
				String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
				
				if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
					if (IPcheck.Database.exemptIP(args[1])) {
						sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.IP_EXEMPT_SUC);
					} else {
						sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.EXEMPTION_FAIL);
					}
				} else {
					if (IPcheck.Database.exemptPlayer(args[1])) {
						sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.PLAYER_EXEMPT_SUC);
					} else {
						sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + TranslationManager.EXEMPTION_FAIL);
					}
				}
			} else {
				sender.sendMessage(TranslationManager.NUM_ARGS_ERR);
			}
		} else {
			sender.sendMessage(TranslationManager.NO_PERM_ERR);
		}
	}

	@Override
	public String getHelp() {
		return "Exempts the IP or player specified from login-checking.";
	}

	@Override
	public String getSyntax() {
		return "exempt <player_name | ip_address>";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.exempt")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Exempt";
	}

}
