package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdAbout implements IpcCommand{
	
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.VER_STRING + " by Jnk1296.");
	}

	@Override
	public int getID() {
		return 11;
	}

	@Override
	public String getHelp() {
		return "Displays Information about IP-Check.";
	}

	@Override
	public String getSyntax() {
		return "about";
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
		return "About";
	}

}
