package net.risenphoenix.jnk.ipcheck.listeners;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.Objects.IPObject;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginCheck {

    public boolean secureCheck(String ip, PlayerLoginEvent e) {
        IPObject ipo = IPcheck.Instance.Database.getAlts(ip);
        int accounts = ipo.getUsers().size();
        Player player = e.getPlayer();
        return secureKick(accounts, player.getName(), e, ip);
    }

    public boolean secureKick(int accounts, String player, PlayerLoginEvent e, String ip) {
        // If the player was reported to have more than the secure-threshold # of accounts, then kick (if not exempt).
        if (accounts > IPcheck.Instance.Configuration.secureThreshold && !IPcheck.Instance.Database.isExemptedPlayer(player) && !IPcheck.Instance.Database.isExemptedIP(ip)) {

            if (player != null) {
                e.setKickMessage(IPcheck.Instance.Configuration.secureKickMsg);
                e.setResult(Result.KICK_OTHER);
                return false;
            }
        }

        return true;
    }
	
}
