package net.risenphoenix.jnk.ipcheck.configuration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.Language;

import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {
	
        private static final Logger logger = Bukkit.getLogger();
        private JavaPlugin plugin;
	private final boolean DEBUG = false;
        
        private File pluginPath = new File("plugins");
 	private File exempt = new File("plugins/IP-check/exempt.lst"); // Exemption List
        private File banned = new File("plugins/IP-check/banned.lst"); // Banned List
 	
 	public static String dateStampFormat = "EEEE, dd MMMM, yyyy 'at' hh:mm:ss a";
 	
 	public String backend = "flatfile";
        public String dbUsername="root";
        public String dbPassword="";
        public String dbName="minecraft";
        public String dbHostname="127.0.0.1";
        public int dbPort=3306;
 	
        
        
 	public boolean secureMode = false;
 	public boolean notifyLogin = true;
 	public boolean detailNotify = false;
 	
 	public int notifyThreshold = 1;
 	public int secureThreshold = 1;
 	
 	public String secureKickMsg = "Multiple Accounts Not Permitted.";
 	public String banMessage = "Banned for Multi-Accounting.";
 	
 	private String defaultExemption =
 			"===============================\r\n" +
 			"Exemptions: IP\r\n" +
 			"===============================\r\n" +
 			"===============================\r\n" +
 			"Exemptions: Player_Name\r\n" +
 			"===============================\r\n" +
 			"===============================\r\n";
 	
        public ConfigurationManager(JavaPlugin plugin){
                this.plugin=plugin;
                initialize();
        }
        public void initialize(){
                plugin.saveConfig();
                plugin.reloadConfig();
                createConfiguration();
                createDefaultDirectory();
 		defaultExemptionList();
                createDefaultStorage();
        }
        
        public void createConfiguration(){
            
        
            dbUsername = plugin.getConfig().getString("dbUsername");
            dbPassword = plugin.getConfig().getString("dbPassword");
            dbName = plugin.getConfig().getString("dbName");
            dbHostname = plugin.getConfig().getString("dbHostname");
            dbPort = plugin.getConfig().getInt("dbPort");
        
            backend = plugin.getConfig().getString("backend");
            notifyLogin = plugin.getConfig().getBoolean("notify-on-login");
            detailNotify = plugin.getConfig().getBoolean("descriptive-notice");
            secureMode = plugin.getConfig().getBoolean("secure-mode");
            dateStampFormat = plugin.getConfig().getString("logging-date-stamp-format");
            notifyThreshold = plugin.getConfig().getInt("min-account-notify-threshold");
            if (notifyThreshold < 1) {
                    logger.warning(Language.PLUG_NAME + "Value of Configuration option 'min-account-notify-threshold' was lower than the minumum limit! 'min-account-notify-threshold' has been set to the default value (1).");
                    notifyThreshold = 1;
            }
            secureThreshold = plugin.getConfig().getInt("secure-kick-threshold");
            if (secureThreshold < 1) {
                    logger.warning(Language.PLUG_NAME + "Value of Configuration option 'secure-kick-threshold' was lower than the minumum limit! 'secure-kick-threshold' has been set to the default value (1).");
                    secureThreshold = 1;
            }
            secureKickMsg = plugin.getConfig().getString("secure-kick-message");
            banMessage = plugin.getConfig().getString("ban-message");
            plugin.saveConfig();
        }
 	
	public void defaultExemptionList() {
		FileWriter f = null;
	    
	    try {
	    	// If configuration file does not exist, create it.
	        if (!exempt.exists()) {
	        	f = new FileWriter(exempt, true);
		        
		        f.write(defaultExemption);
	        }

	    } catch (Exception e) {
	    	ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
	    	logger.info( Language.exmpGenErr);
	    } finally {
	    	try {
	    		if (f != null) {
	    			f.close();
	    		}
    		} catch (Exception e) {
    			ErrorLogger EL = new ErrorLogger();
    			EL.execute(e);
    			logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
    		}
	    }
	}
        
        public void createDefaultStorage() {
            FileWriter f = null;

            try {
                // If Banned Info file does not exist, create it.
                if (!banned.exists()) {
                        f = new FileWriter(banned, true);

                        f.write("### IP-Check Banned List ###\r\n");
                }

            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger();
                        EL.execute(e);
                logger.info(Language.banGenErr);
            } finally {
                try {
                        if (f != null) {
                                f.close();
                        }
                } catch (Exception e) {
                        ErrorLogger EL = new ErrorLogger();
                        EL.execute(e);
                        logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
                }
            }
        }
        
        public void writeBannedList(ArrayList<String> newBannedList) {
            FileWriter f = null;
		
            try {
                if (banned.exists()) {
                    banned.delete();
                }

                f = new FileWriter(banned, true);

                for(String s:newBannedList) {
                    f.write(s + "\r\n");
                }
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger();
                EL.execute(e);
                logger.severe(Language.PLUG_NAME +  Language.banWriteErr);
                logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
            } finally {
                try {
                    if (f != null) {
                        f.close();
                    }
                } catch (Exception e) {
                    ErrorLogger EL = new ErrorLogger();
                    EL.execute(e);
                    logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
                }
            }
        }
        
        public void writeBannedEntry(String playerName, String reason) {
            if (!banned.exists()) {
                createDefaultStorage();
            }
            
            ArrayList<String> bannedList = getPluginBanlist();
            
            boolean shouldReplace = false;
            String value = (playerName + " | " + reason);
            String regex = new String();
            
            for (String e:bannedList) {
                if (e.contains(playerName + " |")) {
                    shouldReplace = true;
                    regex = e;
                    break;
                }
            }
            
            if (shouldReplace) {
                bannedList.remove(regex);
            }
            
            bannedList.add(value);
            
            writeBannedList(bannedList);
        }
	
	/*** Fetches Exemption List from File ***/
	public ArrayList<String> getExemptList() {
		ArrayList<String> exemptList = new ArrayList<String>();
		BufferedReader br = null;
		
		try {
			FileInputStream fstream = new FileInputStream(exempt);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				exemptList.add(strLine);
			}
		} catch (Exception e) {
			ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
			logger.severe(Language.PLUG_NAME +  Language.exmpReadErr);
			logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				ErrorLogger EL = new ErrorLogger();
				EL.execute(e);
				logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
			}
		}
		
		return exemptList;
	}
        
        public ArrayList<String> getPluginBanlist() {
            ArrayList<String> bannedList = new ArrayList<String>();
            BufferedReader br = null;
            
            try {
                FileInputStream fstream = new FileInputStream(banned);
                DataInputStream in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                
                while ((strLine = br.readLine()) != null) {
                    bannedList.add(strLine);
                }
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger();
                EL.execute(e);
                logger.severe(Language.PLUG_NAME +  Language.banReadErr);
                logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    } 
                } catch (Exception e) {
                    ErrorLogger EL = new ErrorLogger();
                    EL.execute(e);
                    logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
                }
            }
            
            return bannedList;
        }
        
        public String getBannedReason(String playerName) {
            ArrayList<String> banlist = getPluginBanlist();
            String reason = "None";
            
            for (String e:banlist) {
                if (e.contains(playerName + " | ")) {
                    reason = e.replace(playerName + " | ", "");
                    break;
                }
            }
            
            return reason;
        }
	
	public boolean addExemption(int exemptionType, String exemption) {
		if (exemptionType == 0) { // IP Exemption
			writeExemptionList(addIpExemption(getExemptList(), exemption));
			return true;
		} else if (exemptionType == 1) { // Player Exemption
			writeExemptionList(addPlayerExemption(getExemptList(), exemption));
			return true;
		}
		
		return false;
	}
	
	// Add a Player Exemption to the configuration
	/*** Writes the value of (String)player to the configuration file.*/
	public ArrayList<String> addPlayerExemption(ArrayList<String> exemptList, String player) {
		ArrayList<String> newExemptionList = new ArrayList<String>();
		String EOC = exemptList.get(exemptList.size() - 1); 
		
		exemptList.set(exemptList.size() - 1, player);
		exemptList.add(EOC);
		
		for (String s:exemptList) newExemptionList.add(s);
		
		return newExemptionList;
	}
	
	public ArrayList<String> addIpExemption(ArrayList<String> exemptionList, String ip) {
		ArrayList<String> newExmp = new ArrayList<String>();
		ArrayList<String> exmpModify = new ArrayList<String>();
		int lineCount = 0;
		int line = 0;
		
		for (String s:exemptionList) {
			if (s.equalsIgnoreCase("Exemptions: IP")) {
				lineCount += 2;
				line = lineCount;
				break;
			}
			
			lineCount++;
		}
		
		while (lineCount < exemptionList.size()) {
			exmpModify.add(exemptionList.get(lineCount));
			lineCount++;
		}
		
		exemptionList.subList(line, exemptionList.size()).clear();
		exemptionList.add(ip);
		
		for (String s:exmpModify) exemptionList.add(s);
		for (String s:exemptionList) newExmp.add(s);
		
		return newExmp;
	}
	
	// Write Modified Exemption List to File
	public void writeExemptionList(ArrayList<String> newExemptList) {
		FileWriter f = null;
		
		try {
			if (exempt.exists()) {
				exempt.delete();
			}
			
        	f = new FileWriter(exempt, true);
	        
	        for(String s:newExemptList) {
	        	f.write(s + "\r\n");
	        }
		} catch (Exception e) {
			ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
	    	logger.severe(Language.PLUG_NAME +  Language.exmpWriteErr);
	    	logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
		} finally {
			try {
				if (f != null) {
					f.close();
				}
			} catch (Exception e) {
				ErrorLogger EL = new ErrorLogger();
				EL.execute(e);
				logger.severe(Language.PLUG_NAME + Language.ERROR_LOG_RMDR);
			}
		}
	}
	
	
	// Returns list of exempt players from Configuration File
	public ArrayList<String> getPlayerExemptList() {
		ArrayList<String> exemptList = new ArrayList<String>();
		ArrayList<String> exempt = getExemptList();
		int line = 0;
		
		for (String s:exempt) {
			if (s.equals("Exemptions: Player_Name")) {
				line+=2;
				break;
			}
			
			line++;
		}
		
		while (!exempt.get(line).contains("=")) {
			exemptList.add(exempt.get(line));
			line++;
		}

		return exemptList;
	}
	
	public ArrayList<String> getIpExemptList() {
		ArrayList<String> exemptList = new ArrayList<String>();
		ArrayList<String> exempt = getExemptList();
		int line = 0;
		
		for (String s:exempt) {
			if (s.equals("Exemptions: IP")) {
				line+=2;
				break;
			}
			
			line++;
		}
		
		while (!exempt.get(line).contains("=")) {
			exemptList.add(exempt.get(line));
			line++;
		}

		return exemptList;
	}
	
	public boolean isExemptPlayer(String player) {
		ArrayList<String> list = getPlayerExemptList();
		
		for (String s:list) {
			if (s.equalsIgnoreCase(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isExemptIp(String ip) {
		ArrayList<String> list = getIpExemptList();
		
		for (String s:list) {
			if (s.equalsIgnoreCase(ip)) {
				return true;
			}
		}
		
		return false;
	}
	
	public int toggle(int toggleID) {
		if (toggleID == 0) {
			if (notifyLogin) {
				notifyLogin = false;
				plugin.getConfig().set("notify-on-login", false);
                                plugin.saveConfig();
				return 0;
			} else if (!notifyLogin){
				notifyLogin = true;
				plugin.getConfig().set("notify-on-login", true);
                                plugin.saveConfig();
				return 1;
			}
		} else if (toggleID == 1) {
			if (detailNotify) {
				detailNotify = false;
				plugin.getConfig().set("descriptive-notice", false);
                                plugin.saveConfig();
				return 0;
			} else {
				detailNotify = true;
				plugin.getConfig().set("descriptive-notice", true);
                                plugin.saveConfig();
				return 1;
			}
		} else if (toggleID == 2) {
			if (secureMode) {
				secureMode = false;
				plugin.getConfig().set("secure-mode", false);
                                plugin.saveConfig();
				return 0;
			} else {
				secureMode = true;
				plugin.getConfig().set("secure-mode", true);
                                plugin.saveConfig();
				return 1;
			}
		}
		
		return 2;
	}


	public boolean deleteExemption(String exemption) {
		ArrayList<String> exemptList = getExemptList();
		int line = 0;
		
		for (String s:exemptList) {
			if (s.equalsIgnoreCase(exemption)) {
				exemptList.remove(line);
				writeExemptionList(exemptList);
				return true;
			}
			
			line++;
		}
		
		return false;
	}
        
        private void createDefaultDirectory() {
            if (!pluginPath.exists())
            pluginPath.mkdir();
        }
}
