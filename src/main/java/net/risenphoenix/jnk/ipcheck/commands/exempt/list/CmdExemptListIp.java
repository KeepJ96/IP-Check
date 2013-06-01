package net.risenphoenix.jnk.ipcheck.commands.exempt.list;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdExemptListIp implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if ((sender.hasPermission("ipcheck.list") && sender.hasPermission("ipcheck.showip")) || sender.isOp()) {
			ArrayList<String> list = IPcheck.Instance.Database.getIpExemptList();
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
			sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NO_PERM_ERR"));
		}
	}

	@Override
	public String getHelp() {
		return "Displays all IPs which are exempt from login-checking.";
	}

	@Override
	public String getSyntax() {
		return "exempt-list ip";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			 new Permission("ipcheck.list"),
			 new Permission("ipcheck.showip")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Exempt-List (IP)";
	}

}
