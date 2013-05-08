package net.risenphoenix.jnk.ipcheck.commands;

import org.bukkit.command.CommandSender;

public interface IpcCommand {

	/** Method called when command is executed. **/
	public void execute(CommandSender sender, String commandLabel, String[] args);
	
	/** Returns the ID of this command. **/
	public int getID();
}
