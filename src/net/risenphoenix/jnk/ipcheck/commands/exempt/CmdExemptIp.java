package net.risenphoenix.jnk.ipcheck.commands.exempt;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdExemptIp implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.exempt") || sender.isOp()) {
			if (args.length == 3) {
				String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
				
				if (args[2].toLowerCase().matches(ip_filter.toLowerCase())) {
					if (Configuration.addExemption(0, args[2])) {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.IP_EXEMPT_SUC);
					} else {
						sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.EXEMPTION_FAIL);
					}
				} else {
					sender.sendMessage(IPcheck.ILL_ARGS_ERR);
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
		return 3;
	}

}
