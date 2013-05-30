package net.risenphoenix.jnk.ipcheck.listeners;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginCheck {

    public boolean secureCheck(String ip, PlayerLoginEvent e) {
        ArrayList<String> players = IPcheck.Database.getAlts(ip);
        int accounts = players.size();
        Player player = e.getPlayer();
        return secureKick(accounts, player.getName(), e, ip);
    }

    public boolean secureKick(int accounts, String player, PlayerLoginEvent e, String ip) {
        // If the player was reported to have more than the secure-threshold # of accounts, then kick (if not exempt).
        if (accounts > IPcheck.Configuration.secureThreshold && !IPcheck.Database.isExemptedPlayer(player) && !IPcheck.Database.isExemptedIP(ip)) {

            if (player != null) {
                e.setKickMessage(IPcheck.Configuration.secureKickMsg);
                e.setResult(Result.KICK_OTHER);
                return false;
            }
        }

        return true;
    }
	
}
