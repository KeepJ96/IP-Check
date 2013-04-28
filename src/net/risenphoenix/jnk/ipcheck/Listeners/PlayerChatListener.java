package net.risenphoenix.jnk.ipcheck.Listeners;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Logging.ExternalLogger;
import net.risenphoenix.jnk.ipcheck.Logging.InternalLogger;
import net.risenphoenix.jnk.ipcheck.Logging.IpcLogger;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerChatListener {

	public void execute(PlayerCommandPreprocessEvent e) {
		char leadingChar = e.getMessage().charAt(1);
		IpcLogger logger;
		
		// Determine which logger to use
		if (Character.toString(leadingChar).equals(IPcheck.ROOT_COMMAND)) {
			logger = new InternalLogger();
		} else {
			logger = new ExternalLogger();
		}
		
		// Log the command
		logger.execute(e);
	}
	
}
