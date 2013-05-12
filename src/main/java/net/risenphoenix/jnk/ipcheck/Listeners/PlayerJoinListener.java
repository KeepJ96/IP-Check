package net.risenphoenix.jnk.ipcheck.Listeners;

import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.LoginReport;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener {

	public void execute (PlayerJoinEvent e) {
		Player player = e.getPlayer();
		LoginReport lp = new LoginReport();
		
		// Do not perform check on operators or players with the "ipcheck.getnotify permission.
		if (!player.isOp() && !player.hasPermission("ipcheck.getnotify")) {
			if (Configuration.notifyLogin && IPcheck.shouldCheck) {
				int accounts = (IPcheck.backend.getAlts(IPcheck.ipToCheck)).size();
				Player playerCheck = e.getPlayer();
				lp.execute(IPcheck.ipToCheck, playerCheck, accounts);
			}
		}
		
		return;
	}
	
}
