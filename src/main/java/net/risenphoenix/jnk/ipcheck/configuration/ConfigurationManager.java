package net.risenphoenix.jnk.ipcheck.configuration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;

import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;

import org.bukkit.Bukkit;

public class ConfigurationManager {
	
        private static final Logger logger = Bukkit.getLogger();
	private final boolean DEBUG = false;
        public static String VER_STRING;
        public static Date COMP_DATE;
 	
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
        
 	public String language = "en";
 	
        public ConfigurationManager(){
            initialize();
        }
        public void initialize(){
            IPcheck.Instance.saveDefaultConfig();
            IPcheck.Instance.reloadConfig();
            readConfiguration();

            VER_STRING = IPcheck.Instance.getDescription().getVersion();
            try {
                COMP_DATE = new Date(IPcheck.class.getResource("IPcheck.class").openConnection().getLastModified()); //Getting compiletime from class file
            } catch (IOException ex) {
                Logger.getLogger(IPcheck.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void readConfiguration(){
            
            dbUsername = IPcheck.Instance.getConfig().getString("dbUsername");
            dbPassword = IPcheck.Instance.getConfig().getString("dbPassword");
            dbName = IPcheck.Instance.getConfig().getString("dbName");
            dbHostname = IPcheck.Instance.getConfig().getString("dbHostname");
            dbPort = IPcheck.Instance.getConfig().getInt("dbPort");
            dbGenerated = IPcheck.Instance.getConfig().getBoolean("dbGenerated");
        
            backend = IPcheck.Instance.getConfig().getString("backend");
            notifyLogin = IPcheck.Instance.getConfig().getBoolean("notify-on-login");
            detailNotify = IPcheck.Instance.getConfig().getBoolean("descriptive-notice");
            secureMode = IPcheck.Instance.getConfig().getBoolean("secure-mode");
            dateStampFormat = IPcheck.Instance.getConfig().getString("logging-date-stamp-format");
            notifyThreshold = IPcheck.Instance.getConfig().getInt("min-account-notify-threshold");
            if (notifyThreshold < 1) {
                    logger.warning(IPcheck.PLUG_NAME + "Value of Configuration option 'min-account-notify-threshold' was lower than the minumum limit! 'min-account-notify-threshold' has been set to the default value (1).");
                    notifyThreshold = 1;
            }
            secureThreshold = IPcheck.Instance.getConfig().getInt("secure-kick-threshold");
            if (secureThreshold < 1) {
                    logger.warning(IPcheck.PLUG_NAME + "Value of Configuration option 'secure-kick-threshold' was lower than the minumum limit! 'secure-kick-threshold' has been set to the default value (1).");
                    secureThreshold = 1;
            }
            secureKickMsg = IPcheck.Instance.getConfig().getString("secure-kick-message");
            banMessage = IPcheck.Instance.getConfig().getString("ban-message");
            
            language = IPcheck.Instance.getConfig().getString("language");
            
            IPcheck.Instance.saveConfig();
        }
 	
	public int toggle(int toggleID) {
		if (toggleID == 0) {
			if (notifyLogin) {
				notifyLogin = false;
				IPcheck.Instance.getConfig().set("notify-on-login", false);
                                IPcheck.Instance.saveConfig();
				return 0;
			} else if (!notifyLogin){
				notifyLogin = true;
				IPcheck.Instance.getConfig().set("notify-on-login", true);
                                IPcheck.Instance.saveConfig();
				return 1;
			}
		} else if (toggleID == 1) {
			if (detailNotify) {
				detailNotify = false;
				IPcheck.Instance.getConfig().set("descriptive-notice", false);
                                IPcheck.Instance.saveConfig();
				return 0;
			} else {
				detailNotify = true;
				IPcheck.Instance.getConfig().set("descriptive-notice", true);
                                IPcheck.Instance.saveConfig();
				return 1;
			}
		} else if (toggleID == 2) {
			if (secureMode) {
				secureMode = false;
				IPcheck.Instance.getConfig().set("secure-mode", false);
                                IPcheck.Instance.saveConfig();
				return 0;
			} else {
				secureMode = true;
				IPcheck.Instance.getConfig().set("secure-mode", true);
                                IPcheck.Instance.saveConfig();
				return 1;
			}
		}
		
		return 2;
	}
}
