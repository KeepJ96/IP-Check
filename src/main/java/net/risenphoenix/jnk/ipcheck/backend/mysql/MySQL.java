package net.risenphoenix.jnk.ipcheck.backend.mysql;
import net.risenphoenix.jnk.ipcheck.backend.Backend;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
/**
 *
 * @author FR34KYN01535
 */
public class MySQL implements Backend{

    private static final Logger logger = Bukkit.getLogger();
    private static String registrar = "MySQL Backend Manager for IP-Check ver 1.0";
    
    private MySQLConnection connection;

    
    @Override
    public void onLoad() {
        initializeBackend();
    }

    @Override
    public void onDisable() {
        // Not used
    }

    @Override
    public void initializeBackend() {
        connection = new MySQLConnection("host.name", "12", "","user", "pass"); 
    }

    @Override
    public String getRegistrar() {
        return registrar;
    }

    @Override
    public ArrayList<String> loadFile(File file) {
        return new ArrayList<String>();
    }

    @Override
    public void log(String player, String ip) {
        //log to db
        
    }

    @Override
    public ArrayList<String> getAlts(String ip) {
        // Convert IP from XXX.XXX.XXX.XXX to XXX_XXX_XXX_XXX
        return new ArrayList<String>();
    }

    @Override
    public boolean isBannedIP(String ip) {
        return false;
    }

    @Override
    public String getLastKnownIP(String player) {
        return "0.0.0.0";
    }
    
    @Override
    public ArrayList<String> getIPs(String player) {
        return new ArrayList<String>();
    }
    
    @Override
    public String checkIPaddress(String ip) {
        //ArrayList<String> check = loadFile(new File("plugins/IP-check/DATABASE/IPS/" + convertIPFormat(ip) + ".log"));
        //if (check == null) {
            return "no-find";
        //}
        
        //return ip;
    }
}
