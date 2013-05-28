package net.risenphoenix.jnk.ipcheck.Listeners;

import java.net.InetAddress;
import net.risenphoenix.jnk.ipcheck.Configuration;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener {

    public static boolean shouldCheck = true;
    public static String ipToCheck = "";
    
    // Mainly deals with logging the IP address
    public void execute(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        LoginCheck lc = new LoginCheck();

        // Construct the IP Address String
        InetAddress a = e.getAddress();
        String iNetAddress = a.getHostAddress();
        StringBuilder ip = new StringBuilder();
        for (int i = 0; i < iNetAddress.length(); i++) {
            if ((iNetAddress.charAt(i) >= '0' && iNetAddress.charAt(i) <= '9') || iNetAddress.charAt(i) == '.') {
                ip.append(iNetAddress.charAt(i));
            } else if (iNetAddress.charAt(i) == ':') {
                break;
            }
        }

        ipToCheck = ip.toString();
        IPcheck.backend.log(player.getName(), ip.toString()); // Log the Playername and IP-Address
        shouldCheck = true;

        if (Configuration.secureMode) {
            shouldCheck = lc.secureCheck(ip.toString(), e);
        }
    }
	
}
