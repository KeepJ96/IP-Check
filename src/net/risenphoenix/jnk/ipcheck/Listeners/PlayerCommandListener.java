package net.risenphoenix.jnk.ipcheck.Listeners;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Logging.ExternalLogger;
import net.risenphoenix.jnk.ipcheck.Logging.InternalLogger;
import net.risenphoenix.jnk.ipcheck.Logging.IpcLogger;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener {

	public void execute(PlayerCommandPreprocessEvent e) {
		String command = e.getMessage();
		command = command.replaceFirst("/", "");
		String[] args = command.split(" ");
		IpcLogger logger;
		
		// Determine which logger to use
		if (args[0].equalsIgnoreCase(IPcheck.ROOT_COMMAND)) {
			logger = new InternalLogger();
		} else {
			logger = new ExternalLogger();
		}
		
		// Log the command
		logger.execute(e);
	}
	
}
