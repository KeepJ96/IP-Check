package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdAbout implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "IP-Check v1.3.0_DEV-RELEASE_01 by Jnk1296.");
	}

	@Override
	public int getID() {
		return 11;
	}

}
