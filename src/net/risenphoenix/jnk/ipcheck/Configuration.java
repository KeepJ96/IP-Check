package net.risenphoenix.jnk.ipcheck;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class Configuration {
	
private static Logger logger = Bukkit.getLogger();
	
	private static final String PLUG_NAME = "[IP-Check] ";

	// File paths
	private static File dir = new File("plugins/IP-check");
 	private static File path = new File("plugins/IP-check/Config.txt");
 	
 	private static String confWriteErr = "Failed to generate Configuration File!";
 	private static String confReadErr = "Failed to read Configuration File!";
 	
 	// 0 = Essentials
 	// 1 = FlatFile
 	public static int backend = 0;
 	
 	public static boolean secureMode = false;
 	public static boolean notifyLogin = true;
 	public static boolean detailNotify = false;
 	
 	public static int notifyThreshold = 1;
 	public static int secureThreshold = 1;
 	
 	public static String secureKickMsg = "Multiple Accounts Not Permitted.";
 	public static String banMessage = "Banned for Multi-Accounting.";
 	
 	private static String defaultConfig =
 			"# IPcheck 1.2.0 Configuration / Exemption List\r\n" +
 			"===============================\r\n" +
 			"Configuration Options\r\n" +
 			"===============================\r\n" +
 			"notify-on-login: true\r\n" +
 			"descriptive-notice: false\r\n" +
 			"secure-mode: false\r\n" +
 			"use-flat-file: false\r\n" +
 			"min-account-notify-threshold: 1\r\n" +
 			"secure-kick-threshold: 1\r\n" +
 			"secure-kick-message: 'Multiple Accounts Not Permitted.'\r\n" +
 			"ban-message: 'Banned for Multi-Accounting.'\r\n" +
 			"===============================\r\n" +
 			"Exemptions: IP\r\n" +
 			"===============================\r\n" +
 			"===============================\r\n" +
 			"Exemptions: Player_Name\r\n" +
 			"===============================\r\n" +
 			"===============================\r\n";

 	public static void onLoad() {
 		defaultConfiguration(); // Generate Default Configuration if one does not exist.
 		checkVersion();
 		parseConfigSettings(getConfiguration()); // Load and parse configuration.
 	}
 	
 	public static void checkVersion() {
 		ArrayList<String> config = new ArrayList<String>();
		BufferedReader br = null;
 		
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			boolean shouldWrite = false;
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains("# IPcheck")) {
					if (!strLine.contains("1.2.0")) {
						shouldWrite = true;
						logger.info(PLUG_NAME + "Updated Configuration File!");
						config.add("# IPcheck 1.2.0 Configuration / Exemption List");
					} else {
						config.add(strLine);
					}
				} else if (strLine.contains("secure-mode: ") && shouldWrite) {
					config.add(strLine);
					config.add("use-flat-file: false");
				} else {
					config.add(strLine);
				}
			}
			
		} catch (Exception e) {
			logger.severe(confReadErr);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
		}
		
		writeConfiguration(config);
 	}
 	
 	// Generate Default Configuration is one is needed.
	public static void defaultConfiguration() {
		FileWriter f = null;
	    
	    try {
	    	// If VoteLinks folder does not exist, create it.
	    	if (!dir.exists()) dir.mkdir();
	        
	    	// If configuration file does not exist, create it.
	        if (!path.exists()) {
	        	f = new FileWriter(path, true);
		        
		        f.write(defaultConfig);
		        f.close();
	        }

	    } catch (IOException e) {
	    	// Console Output (in the event of an exception)
	    	e.printStackTrace();
	    	logger.info(confWriteErr);
	    }
	}
	
	/***
	 * Fetches Current Configuration from File for use in editing.
	 * 
	 * @return ArrayList<String> containing the current Configuration.
	 */
	public static ArrayList<String> getConfiguration() {
		ArrayList<String> config = new ArrayList<String>();
		
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				config.add(strLine);
			}
			
			br.close();
		} catch (Exception e) {
			logger.severe(confReadErr);
		}
		
		return config;
	}
	
	// Generate a Blank Configuration Document
	public static void regenerateConfiguration() {
		if (path.exists()) {
			path.delete();
		}
		
		defaultConfiguration();
	}
	
	public static boolean addExemption(int exemptionType, String exemption) {
		if (exemptionType == 0) { // IP Exemption
			writeConfiguration(addIpExemption(getConfiguration(), exemption));
			getConfiguration();
			return true;
		} else if (exemptionType == 1) { // Player Exemption
			writeConfiguration(addPlayerExemption(getConfiguration(), exemption));
			getConfiguration();
			return true;
		}
		
		return false;
	}
	
	// Add a Player Exemption to the configuration
	/*** Writes the value of (String)player to the configuration file.*/
	public static ArrayList<String> addPlayerExemption(ArrayList<String> config, String player) {
		ArrayList<String> newConfig = new ArrayList<String>();
		String EOC = config.get(config.size() - 1); 
		
		config.set(config.size() - 1, player);
		config.add(EOC);
		
		for (String s:config) newConfig.add(s);
		
		return newConfig;
	}
	
	public static ArrayList<String> addIpExemption(ArrayList<String> config, String ip) {
		ArrayList<String> newConfig = new ArrayList<String>();
		ArrayList<String> confBlock = new ArrayList<String>();
		int lineCount = 0;
		int line = 0;
		
		for (String s:config) {
			if (s.equalsIgnoreCase("Exemptions: IP")) {
				lineCount += 2;
				line = lineCount;
				break;
			}
			
			lineCount++;
		}
		
		while (lineCount < config.size()) {
			confBlock.add(config.get(lineCount));
			lineCount++;
		}
		
		config.subList(line, config.size()).clear();
		config.add(ip);
		
		for (String s:confBlock) config.add(s);
		for (String s:config) newConfig.add(s);
		
		return newConfig;
	}
	
	// Write Modified Configuration to File
	public static void writeConfiguration(ArrayList<String> newConfig) {
		FileWriter f = null;
		
		try {
			if (path.exists()) {
				path.delete();
			}
			
        	f = new FileWriter(path, true);
	        
	        for(String s:newConfig) {
	        	f.write(s + "\r\n");
	        }
	        
	        f.close();
		} catch (Exception e) {
			e.printStackTrace();
	    	logger.severe(confWriteErr);
		}
	}
	
	public static void parseConfigSettings(ArrayList<String> config) {
		String modulus;
		
		for (String line:config) {
			// Should use Login Checking?
			if (line.contains("notify-on-login: ")) {
				modulus = line.replace("notify-on-login: ", "");
				if (modulus.equalsIgnoreCase("true")) {
					notifyLogin = true;
				} else {
					notifyLogin = false;
				}
				
			// Should show descriptive notifications?
			} else if (line.contains("descriptive-notice: ")) {
				modulus = line.replace("descriptive-notice: ", "");
				if (modulus.equalsIgnoreCase("true")) {
					detailNotify = true;
				} else {
					detailNotify = false;
				}
			
			// Should use Secure Mode?
			} else if (line.contains("secure-mode: ")) {
				modulus = line.replace("secure-mode: ", "");
				if (modulus.equalsIgnoreCase("true")) {
					secureMode = true;
				} else {
					secureMode = false;
				}
				
			// Should use Database?
			} else if (line.contains("use-flat-file: ")) {
				modulus = line.replace("use-flat-file: ", "");
				if (modulus.equalsIgnoreCase("true")) {
					backend = 1;
				} else {
					backend = 0;
				}
								
			// Set a minimum number of accounts to have before being notified
			} else if (line.contains("min-account-notify-threshold: ")) {
				modulus = line.replace("min-account-notify-threshold: ", "");
				try {
					notifyThreshold = Integer.parseInt(modulus);
					if (notifyThreshold < 1) {
						logger.warning("Value of Configuration option 'min-account-notify-threshold' was lower than the minumum limit! 'min-account-notify-threshold' has been set to the default value (1).");
						notifyThreshold = 1;
					}
				} catch (NumberFormatException e) {
					logger.warning("Failed to parse Configuration option 'min-account-notify-threshold': was not valid integer.");
				}
			
			// Set a minimum number of accounts to have before being kicked
			} else if (line.contains("secure-kick-threshold: ")) {
				modulus = line.replace("secure-kick-threshold: ", "");
				try {
					secureThreshold = Integer.parseInt(modulus);
					if (secureThreshold < 1) {
						logger.warning("Value of Configuration option 'secure-kick-threshold' was lower than the minumum limit! 'secure-kick-threshold' has been set to the default value (1).");
						secureThreshold = 1;
					}
				} catch (NumberFormatException e) {
					logger.warning("Failed to parse Configuration option 'secure-kick-threshold': was not valid integer.");
				}
				
			// Set Kick Message
			} else if (line.contains("secure-kick-message: ")) {
				modulus = line.replace("secure-kick-message: ", "");
				secureKickMsg = modulus;
				
			// Set Ban Message
			} else if (line.contains("ban-message: ")) {
				modulus = line.replace("ban-message: ", "");
				banMessage = modulus;
			}
		}
	}
	
	// Returns list of exempt players from Configuration File
	public static ArrayList<String> getPlayerExemptList() {
		ArrayList<String> exemptList = new ArrayList<String>();
		ArrayList<String> config = getConfiguration();
		int line = 0;
		
		for (String s:config) {
			if (s.equals("Exemptions: Player_Name")) {
				line+=2;
				break;
			}
			
			line++;
		}
		
		while (!config.get(line).contains("=")) {
			exemptList.add(config.get(line));
			line++;
		}

		return exemptList;
	}
	
	public static ArrayList<String> getIpExemptList() {
		ArrayList<String> exemptList = new ArrayList<String>();
		ArrayList<String> config = getConfiguration();
		int line = 0;
		
		for (String s:config) {
			if (s.equals("Exemptions: IP")) {
				line+=2;
				break;
			}
			
			line++;
		}
		
		while (!config.get(line).contains("=")) {
			exemptList.add(config.get(line));
			line++;
		}

		return exemptList;
	}
	
	public static boolean isExemptPlayer(String player) {
		ArrayList<String> list = getPlayerExemptList();
		
		for (String s:list) {
			if (s.equalsIgnoreCase(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isExemptIp(String ip) {
		ArrayList<String> list = getIpExemptList();
		
		for (String s:list) {
			if (s.equalsIgnoreCase(ip)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static int toggle(int toggleID) {
		if (toggleID == 0) {
			if (notifyLogin) {
				notifyLogin = false;
				toggleNotifyMode(getConfiguration(), notifyLogin);
				return 0;
			} else {
				notifyLogin = true;
				toggleNotifyMode(getConfiguration(), notifyLogin);
				return 1;
			}
		} else if (toggleID == 1) {
			if (detailNotify) {
				detailNotify = false;
				toggleNotifyDetail(getConfiguration(), detailNotify);
				return 0;
			} else {
				detailNotify = true;
				toggleNotifyDetail(getConfiguration(), detailNotify);
				return 1;
			}
		} else if (toggleID == 2) {
			if (secureMode) {
				secureMode = false;
				toggleSecureMode(getConfiguration(), secureMode);
				return 0;
			} else {
				secureMode = true;
				toggleSecureMode(getConfiguration(), secureMode);
				return 1;
			}
		}
		
		return 2;
	}
	
	public static void toggleSecureMode(ArrayList<String> config, boolean value) {
		int line = 0;
		
		for (String s:config) {
			if (s.contains("secure-mode: ")) {
				config.set(line, "secure-mode: " + value);
				break;
			}
			
			line++;
		}
		writeConfiguration(config);
		parseConfigSettings(getConfiguration());
	}
	
	public static void toggleNotifyMode(ArrayList<String> config, boolean value) {
		int line = 0;
		
		for (String s:config) {
			if (s.contains("notify-on-login: ")) {
				config.set(line, "notify-on-login: " + value);
				break;
			}
			
			line++;
		}
		writeConfiguration(config);
		parseConfigSettings(getConfiguration());
	}

	public static void toggleNotifyDetail(ArrayList<String> config, boolean value) {
		int line = 0;
		
		for (String s:config) {
			if (s.contains("descriptive-notice: ")) {
				config.set(line, "descriptive-notice: " + value);
				break;
			}
			
			line++;
		}
		writeConfiguration(config);
		parseConfigSettings(getConfiguration());
	}
	
	public static boolean deleteExemption(String exemption) {
		ArrayList<String> config = getConfiguration();
		int line = 0;
		
		for (String s:config) {
			if (s.equalsIgnoreCase(exemption)) {
				config.remove(line);
				writeConfiguration(config);
				getConfiguration();
				return true;
			}
			
			line++;
		}
		
		return false;
	}

}