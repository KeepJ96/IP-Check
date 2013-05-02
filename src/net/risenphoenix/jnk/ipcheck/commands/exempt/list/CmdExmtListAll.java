package net.risenphoenix.jnk.ipcheck.commands.exempt.list;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdExmtListAll implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.list") || sender.isOp()) {
			ArrayList<String> list = Configuration.getPlayerExemptList();
			ArrayList<String> list2 = Configuration.getIpExemptList();
			
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
	
			sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
			for (String s:list) {
				sb1.append(s + ", ");
			}
			sender.sendMessage(sb1.toString());
			
			sender.sendMessage(ChatColor.GOLD + "------------------------------");
			
			if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
				for (String s:list2) {
					sb2.append(s + ", ");
				}
				sender.sendMessage(sb2.toString());
			} else {
				sender.sendMessage(IPcheck.NO_PERM_ERR);
			}
			
			sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
			sender.sendMessage(ChatColor.YELLOW + "Total exemptions on file: " + ChatColor.LIGHT_PURPLE + (list.size() + list2.size()));
			sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		} else {
			sender.sendMessage(IPcheck.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 7;
	}

}
