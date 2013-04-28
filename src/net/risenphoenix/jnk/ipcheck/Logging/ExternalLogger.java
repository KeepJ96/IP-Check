package net.risenphoenix.jnk.ipcheck.Logging;

import java.util.logging.Logger;

import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ExternalLogger implements IpcLogger{

	@Override
	public void execute(PlayerCommandPreprocessEvent e) {
		Logger logger = Bukkit.getLogger();
		DateStamp ds = new DateStamp();
		
		String playerName = e.getPlayer().getName();
		
		//For Debugging Purposes
		logger.info(IPcheck.PLUG_NAME + "User " + playerName + " issued command: '" + e.getMessage() + 
				"' on server " + Bukkit.getServerName() + " on " + ds.getDateStamp() + "."
		);
		
		//TODO
		// MySQL Logging Code to go here when test DB is set up.
	}

}
