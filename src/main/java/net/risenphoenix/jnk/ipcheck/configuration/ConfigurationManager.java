package net.risenphoenix.jnk.ipcheck.configuration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;

import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;

import org.bukkit.Bukkit;

public class ConfigurationManager {
	
        private static final Logger logger = Bukkit.getLogger();
	private final boolean DEBUG = false;
        
 	
 	public static String dateStampFormat = "EEEE, dd MMMM, yyyy 'at' hh:mm:ss a";
 	
 	public String backend = "flatfile";
        public String dbUsername="root";
        public String dbPassword="";
        public String dbName="minecraft";
        public String dbHostname="127.0.0.1";
        public boolean dbGenerated=true;
        public int dbPort=3306;
 	
        
        
 	public boolean secureMode = false;
 	public boolean notifyLogin = true;
 	public boolean detailNotify = false;
 	
 	public int notifyThreshold = 1;
 	public int secureThreshold = 1;
 	
 	public String secureKickMsg = "Multiple Accounts Not Permitted.";
 	public String banMessage = "Banned for Multi-Accounting.";
 	
        public ConfigurationManager(){
                initialize();
        }
        public void initialize(){
                IPcheck.getInstance().saveConfig();
                IPcheck.getInstance().reloadConfig();
                readConfiguration();
        }
        
        public void readConfiguration(){
            
            dbUsername = IPcheck.getInstance().getConfig().getString("dbUsername");
            dbPassword = IPcheck.getInstance().getConfig().getString("dbPassword");
            dbName = IPcheck.getInstance().getConfig().getString("dbName");
            dbHostname = IPcheck.getInstance().getConfig().getString("dbHostname");
            dbPort = IPcheck.getInstance().getConfig().getInt("dbPort");
            dbGenerated = IPcheck.getInstance().getConfig().getBoolean("dbGenerated");
        
            backend = IPcheck.getInstance().getConfig().getString("backend");
            notifyLogin = IPcheck.getInstance().getConfig().getBoolean("notify-on-login");
            detailNotify = IPcheck.getInstance().getConfig().getBoolean("descriptive-notice");
            secureMode = IPcheck.getInstance().getConfig().getBoolean("secure-mode");
            dateStampFormat = IPcheck.getInstance().getConfig().getString("logging-date-stamp-format");
            notifyThreshold = IPcheck.getInstance().getConfig().getInt("min-account-notify-threshold");
            if (notifyThreshold < 1) {
                    logger.warning(TranslationManager.PLUG_NAME + "Value of Configuration option 'min-account-notify-threshold' was lower than the minumum limit! 'min-account-notify-threshold' has been set to the default value (1).");
                    notifyThreshold = 1;
            }
            secureThreshold = IPcheck.getInstance().getConfig().getInt("secure-kick-threshold");
            if (secureThreshold < 1) {
                    logger.warning(TranslationManager.PLUG_NAME + "Value of Configuration option 'secure-kick-threshold' was lower than the minumum limit! 'secure-kick-threshold' has been set to the default value (1).");
                    secureThreshold = 1;
            }
            secureKickMsg = IPcheck.getInstance().getConfig().getString("secure-kick-message");
            banMessage = IPcheck.getInstance().getConfig().getString("ban-message");
            IPcheck.getInstance().saveConfig();
        }
 	
	public int toggle(int toggleID) {
		if (toggleID == 0) {
			if (notifyLogin) {
				notifyLogin = false;
				IPcheck.getInstance().getConfig().set("notify-on-login", false);
                                IPcheck.getInstance().saveConfig();
				return 0;
			} else if (!notifyLogin){
				notifyLogin = true;
				IPcheck.getInstance().getConfig().set("notify-on-login", true);
                                IPcheck.getInstance().saveConfig();
				return 1;
			}
		} else if (toggleID == 1) {
			if (detailNotify) {
				detailNotify = false;
				IPcheck.getInstance().getConfig().set("descriptive-notice", false);
                                IPcheck.getInstance().saveConfig();
				return 0;
			} else {
				detailNotify = true;
				IPcheck.getInstance().getConfig().set("descriptive-notice", true);
                                IPcheck.getInstance().saveConfig();
				return 1;
			}
		} else if (toggleID == 2) {
			if (secureMode) {
				secureMode = false;
				IPcheck.getInstance().getConfig().set("secure-mode", false);
                                IPcheck.getInstance().saveConfig();
				return 0;
			} else {
				secureMode = true;
				IPcheck.getInstance().getConfig().set("secure-mode", true);
                                IPcheck.getInstance().saveConfig();
				return 1;
			}
		}
		
		return 2;
	}
}
