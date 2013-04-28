package net.risenphoenix.jnk.ipcheck.Logging;

import java.util.logging.Logger;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.commands.ParseCommand;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class InternalLogger implements IpcLogger{

	@Override
	public void execute(PlayerCommandPreprocessEvent e) {
		Logger logger = Bukkit.getLogger();
		DateStamp ds = new DateStamp();
		
		String sender = e.getPlayer().getName();
		
		// Get Command ID
		String message = e.getMessage();
		message = message.replaceFirst("/c ", "");
		String[] args = message.split(" ");
		
		int commandID = ParseCommand.execute(args);
		
		//For Debugging Purposes
		logger.info(IPcheck.PLUG_NAME + "User " + sender + " issued command: '" + e.getMessage() + 
				"' (Command ID: " + commandID + ") on server " + Bukkit.getServerName() + " on " + 
				ds.getDateStamp() + "."
		);
		
		//TODO
		// MySQL Logging Code to go here when test DB is set up. 
	}
	
}
