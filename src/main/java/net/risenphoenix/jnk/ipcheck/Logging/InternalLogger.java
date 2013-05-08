package net.risenphoenix.jnk.ipcheck.Logging;

import java.util.logging.Logger;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.commands.ParseCommand;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class InternalLogger implements IpcLogger{

	@Override
	public void execute(PlayerCommandPreprocessEvent e) {
		// Instantiate Logger and DateStamp systems
		Logger logger = Bukkit.getLogger();
		DateStamp ds = new DateStamp();
		
		// Get sender name
		String sender = e.getPlayer().getName();
		
		// Create Arguments Array for parsing
		String message = e.getMessage();
		message = message.replaceFirst("/c ", "");
		String[] args = message.split(" ");
		
		// Get Internal Command ID
		int commandID = ParseCommand.execute(args);
		
		//Log to Database
		if (commandID >= 0) {
			// This log only for debugging purposes
			logger.info(IPcheck.PLUG_NAME + "User " + sender + " issued command: '" + e.getMessage() + 
					"' (Command ID: " + commandID + ") on server " + Bukkit.getServerName() + " on " + 
					ds.getDateStamp() + "."
			);
			
			//TODO
			// MySQL Logging Code to go here when test DB is set up. 
		}
	}
	
}
