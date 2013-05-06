package net.risenphoenix.jnk.ipcheck.commands.exempt.list;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdExmtListPlayer implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.list") || sender.isOp()) {
			ArrayList<String> list = Configuration.getPlayerExemptList();
			StringBuilder sb = new StringBuilder();
	
			sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
			
			for (String s:list) {
				sb.append(s + ", ");
			}
	
			sender.sendMessage(sb.toString());
			
			sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
			sender.sendMessage(ChatColor.YELLOW + "Total players in exemption list: " + ChatColor.LIGHT_PURPLE + list.size());
			sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
		} else {
			sender.sendMessage(IPcheck.NO_PERM_ERR);
		}
	}

	@Override
	public int getID() {
		return 9;
	}

	@Override
	public String getHelp() {
		return "Displays all players who are exempt from login-checking.";
	}

	@Override
	public String getSyntax() {
		return "exempt-list player";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.list")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Exempt-List (Player)";
	}

}
