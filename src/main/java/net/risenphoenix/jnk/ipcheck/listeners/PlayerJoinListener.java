package net.risenphoenix.jnk.ipcheck.listeners;

import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.reports.LoginReport;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener {

    private PlayerLoginListener PLL = IPcheck.PLL;
    
    public void execute (PlayerJoinEvent e) {
        Player player = e.getPlayer();
        LoginReport lr = new LoginReport();

        // Do not perform check on operators or players with the "ipcheck.getnotify permission.
        if (!player.isOp() && !player.hasPermission("ipcheck.getnotify")) {
            if (IPcheck.Instance.Configuration.notifyLogin && PLL.shouldCheck) {
                int accounts = (IPcheck.Instance.Database.getAlts(PLL.ipToCheck)).getUsers().size();
                Player playerCheck = e.getPlayer();
                lr.execute(PLL.ipToCheck, playerCheck, accounts);
            }
        }
    }	
}
