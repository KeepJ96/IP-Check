package net.risenphoenix.jnk.ipcheck;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.risenphoenix.jnk.ipcheck.Logging.ErrorLogger;

import org.bukkit.Bukkit;

public class Configuration {
	
private static Logger logger = Bukkit.getLogger();
	
	private static final boolean DEBUG = false;

	private static final String PLUG_NAME = "[IP-Check] ";

	// File paths
        private static File pluginPath = new File("plugins");
	private static File dir = new File("plugins/IP-check"); // Plugin Directory
 	private static File path = new File("plugins/IP-check/Config.txt"); // Configuration File
 	private static File exempt = new File("plugins/IP-check/exempt.lst"); // Exemption List
 	
 	private static String confWriteErr = "Failed to generate Configuration File!";
 	private static String confReadErr = "Failed to read Configuration File!";
 	private static String exmpWriteErr = "Failed to write to Exemption File!";
 	private static String exmpReadErr = "Failed to read Exemption File!";
 	private static String exmpGenErr = "Failed to generate Exemption File!";
 	
 	private static String COE1 = "Failed to parse configuration option: ";
 	private static String COE2 = ". Is the configuration file formatted correctly?";
 	
 	public static String dateStampFormat = "EEEE, dd MMMM, yyyy 'at' hh:mm:ss a";
 	
 	// 0 = Essentials
 	// 1 = FlatFile
 	public static int backend = 0;
 	
 	public static boolean secureMode = false;
 	public static boolean notifyLogin = true;
 	public static boolean detailNotify = false;
 	public static boolean usingMySQL = false;
 	
 	public static int notifyThreshold = 1;
 	public static int secureThreshold = 1;
 	
 	public static String secureKickMsg = "Multiple Accounts Not Permitted.";
 	public static String banMessage = "Banned for Multi-Accounting.";
 	
 	private static String mySQLdb = null;
 	private static String mySQLuser = null;
 	private static String mySQLpassword = null;
 	
 	private static String defaultConfig =
 			"# IP-Check 1.3.0 Configuration / Exemption List\r\n" +						// 0
 			"===============================\r\n" + 									// 1
 			"Configuration Options\r\n" +        										// 2
 			"===============================\r\n" +										// 3			
 			"notify-on-login: true\r\n" +												// 4
 			"descriptive-notice: false\r\n" +											// 5
 			"secure-mode: false\r\n" +													// 6
 			"use-flat-file: false\r\n" +												// 7 - Added Update #1
 			"min-account-notify-threshold: 1\r\n" +										// 8
 			"secure-kick-threshold: 1\r\n" +											// 9
 			"secure-kick-message: Multiple Accounts Not Permitted.\r\n" +				// 10
 			"ban-message: Banned for Multi-Accounting.\r\n" +							// 11
 			"logging-date-stamp-format: EEEE, MMMM dd, yyyy 'at' hh:mm:ss a, ZZZ\r\n" +	// 12 - Added Update #7
 			"-------------------------------\r\n" +										// 13
 			"use-mysql: false\r\n" +													// 14
 			"database-address: \r\n" +													// 15
 			"user-name: \r\n" +															// 16
 			"password: ";																// 17

 	private static String defaultExemption =
 			"===============================\r\n" +
 			"Exemptions: IP\r\n" +
 			"===============================\r\n" +
 			"===============================\r\n" +
 			"Exemptions: Player_Name\r\n" +
 			"===============================\r\n" +
 			"===============================\r\n";
 	
 	public static void onLoad() {
                createDefaultDirectory();
 		defaultConfiguration(); // Generate Default Configuration if one does not exist.
 		defaultExemptionList();
 		parseConfigSettings(getConfiguration()); // Load and parse configuration.
 		checkVersion(); // Update Configuration
 	}
 	
 	public static void checkVersion() {
 		ArrayList<String> config = new ArrayList<String>();
		BufferedReader br = null;
 		
		// Load Config from file
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				config.add(strLine);
			}
			
		} catch (Exception e) {
			ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
			logger.severe(PLUG_NAME + confReadErr);
			logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				ErrorLogger EL = new ErrorLogger();
				EL.execute(e);
				logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
			}
		}
		
		// Determine if the exemption sub-section of the configuration file exists, and if so, remove it and save it to a new file.
		if (config.contains("Exemptions: IP") || config.contains("Exemptions: Player_Name")) {
			ArrayList<String> exemptSplit = new ArrayList<String>();
			int lineCount = 0;
			
			// Find where in the configuration file the exemption section is
			while(!config.get(lineCount).equalsIgnoreCase("Exemptions: IP") && !config.get(lineCount).equalsIgnoreCase("Exemptions: Player_Name")) {
				lineCount++;
			}
			
			// Backup one line to encompass the "====" header.
			lineCount -= 1;
			
			// Add the exemption block to the new exemptSplit list
			while(lineCount < config.size()) {
				exemptSplit.add(config.get(lineCount));
				lineCount++;
			}
			
			writeExemptionList(exemptSplit);
		}
		
		// Check if Configuration Header indicates current version, else update.
		if (!config.get(0).contains("1.3.0")) {
			logger.info(PLUG_NAME + "Updating Configuration File!");
		} else {
			return; // Configuration is already up to date. There is no need to go further.
		}
		
		ArrayList<String> currentConfiguration = getConfiguration();
		ArrayList<String> newConfig = new ArrayList<String>(); // Contains the updated configuration
		String[] blankConfiguration = defaultConfig.split("\r\n");
		
		// Convert blankConfiguration to arrayList
		for(int i = 0; i < blankConfiguration.length; i++) {
			newConfig.add(blankConfiguration[i]);
		}
		
		// Create a new Configuration file with the settings of the old configuration.
		for (int i = 4; i < newConfig.size(); i++) {
			StringBuilder sb = new StringBuilder();
			int charCount = 0;
			
			// Get Option Text
			while(newConfig.get(i).charAt(charCount) != ':') {
				sb.append(newConfig.get(i).charAt(charCount));
				charCount++;
				
				// To prevent outOfBounds exception
				if (charCount >= newConfig.get(i).length()) {
					break;
				}
			}
			
			// Scan for option in current configuration, and if it's found, set the corresponding option in new config.
			for(String s:currentConfiguration) {
				if (s.contains(sb.toString())) {
					newConfig.set(i, s);
					break;
				}
			}
		}
		
		if (DEBUG) {
			logger.warning(PLUG_NAME + "DEBUG MODE IS ENABLED! IF YOU SEE THIS MESSAGE, PLEASE ALERT THE DEVELOPER.");
			logger.warning(PLUG_NAME + "DISPLAYING MODIFIED CONFIGURATION. THIS CONFIGURATION WILL NOT BE SAVED TO FILE.");
			logger.info("");
			for(String s:newConfig) {
				logger.info(PLUG_NAME + s);
			}
			logger.info("");
		}
		
		if (!DEBUG) writeConfiguration(newConfig);
 	}
 	
 	// Generate Default Configuration is one is needed.
	public static void defaultConfiguration() {
		FileWriter f = null;
	    
	    try {
	    	// If IP-Check folder does not exist, create it.
	    	if (!dir.exists()) dir.mkdir();
	        
	    	// If configuration file does not exist, create it.
	        if (!path.exists()) {
	        	f = new FileWriter(path, true);
		        
		        f.write(defaultConfig);
		        f.close();
	        }

	    } catch (Exception e) {
	    	ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
	    	logger.info(PLUG_NAME + confWriteErr);
	    	logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
	    }
	}
	
	public static void defaultExemptionList() {
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
	    	logger.info(exmpGenErr);
	    } finally {
	    	try {
	    		if (f != null) {
	    			f.close();
	    		}
    		} catch (Exception e) {
    			ErrorLogger EL = new ErrorLogger();
    			EL.execute(e);
    			logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
    		}
	    }
	}
	
	public static String getMySQLUsername() {
		return mySQLuser;
	}
	
	public static String getMySQLpassword() {
		return mySQLpassword;
	}
	
	public static String getMySQLdatabase() {
		return mySQLdb;
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
			ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
			logger.severe(PLUG_NAME + confReadErr);
			logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
		}
		
		return config;
	}
	
	/*** Fetches Exemption List from File ***/
	public static ArrayList<String> getExemptList() {
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
			logger.severe(PLUG_NAME + exmpReadErr);
			logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				ErrorLogger EL = new ErrorLogger();
				EL.execute(e);
				logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
			}
		}
		
		return exemptList;
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
	public static ArrayList<String> addPlayerExemption(ArrayList<String> exemptList, String player) {
		ArrayList<String> newExemptionList = new ArrayList<String>();
		String EOC = exemptList.get(exemptList.size() - 1); 
		
		exemptList.set(exemptList.size() - 1, player);
		exemptList.add(EOC);
		
		for (String s:exemptList) newExemptionList.add(s);
		
		return newExemptionList;
	}
	
	public static ArrayList<String> addIpExemption(ArrayList<String> exemptionList, String ip) {
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
	public static void writeExemptionList(ArrayList<String> newExemptList) {
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
	    	logger.severe(PLUG_NAME + exmpWriteErr);
	    	logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
		} finally {
			try {
				if (f != null) {
					f.close();
				}
			} catch (Exception e) {
				ErrorLogger EL = new ErrorLogger();
				EL.execute(e);
				logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
			}
		}
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
			ErrorLogger EL = new ErrorLogger();
			EL.execute(e);
	    	logger.severe(PLUG_NAME + confWriteErr);
	    	logger.severe(PLUG_NAME + IPcheck.ERROR_LOG_RMDR);
		}
	}
	
	public static void parseConfigSettings(ArrayList<String> config) {
		String modulus;
		
		for (String line:config) {
			// Should use Login Checking?
			if (line.contains("notify-on-login:")) {
				modulus = line.replace("notify-on-login:", "");
				modulus = modulus.trim();
				if (modulus.equalsIgnoreCase("true")) {
					notifyLogin = true;
				} else if (modulus.equalsIgnoreCase("false")) {
					notifyLogin = false;
				} else {
					logger.severe(PLUG_NAME + COE1 + "notify-on-login" + COE2);
				}
				
			// Should show descriptive notifications?
			} else if (line.contains("descriptive-notice:")) {
				modulus = line.replace("descriptive-notice:", "");
				modulus = modulus.trim();
				if (modulus.equalsIgnoreCase("true")) {
					detailNotify = true;
				} else if (modulus.equalsIgnoreCase("false")) {
					detailNotify = false;
				} else {
					logger.severe(PLUG_NAME + COE1 + "descriptive-notice" + COE2);
				}
			
			// Should use Secure Mode?
			} else if (line.contains("secure-mode:")) {
				modulus = line.replace("secure-mode:", "");
				modulus = modulus.trim();
				if (modulus.equalsIgnoreCase("true")) {
					secureMode = true;
				} else if (modulus.equalsIgnoreCase("false")){
					secureMode = false;
				} else {
					logger.severe(PLUG_NAME + COE1 + "secure-mode" + COE2);
				}
				
			// Should use Database?
			} else if (line.contains("use-flat-file:")) {
				modulus = line.replace("use-flat-file:", "");
				modulus = modulus.trim();
				if (modulus.equalsIgnoreCase("true")) {
					backend = 1;
				} else if (modulus.equalsIgnoreCase("false")) {
					backend = 0;
				} else {
					logger.severe(PLUG_NAME + COE1 + "use-flat-file" + COE2);
				}
				
			// Set Logging Time/Date Stamp Format
			} else if (line.contains("logging-date-stamp-format:")) {
				modulus = line.replace("logging-date-stamp-format:", "");
				modulus = modulus.trim();
				dateStampFormat = modulus;
								
			// Set a minimum number of accounts to have before being notified
			} else if (line.contains("min-account-notify-threshold:")) {
				modulus = line.replace("min-account-notify-threshold:", "");
				modulus = modulus.trim();
				try {
					notifyThreshold = Integer.parseInt(modulus);
					if (notifyThreshold < 1) {
						logger.warning(PLUG_NAME + "Value of Configuration option 'min-account-notify-threshold' was lower than the minumum limit! 'min-account-notify-threshold' has been set to the default value (1).");
						notifyThreshold = 1;
					}
				} catch (NumberFormatException e) {
					logger.warning(PLUG_NAME + "Failed to parse Configuration option 'min-account-notify-threshold': was not valid integer.");
				}
			
			// Set a minimum number of accounts to have before being kicked
			} else if (line.contains("secure-kick-threshold:")) {
				modulus = line.replace("secure-kick-threshold:", "");
				modulus = modulus.trim();
				try {
					secureThreshold = Integer.parseInt(modulus);
					if (secureThreshold < 1) {
						logger.warning(PLUG_NAME + "Value of Configuration option 'secure-kick-threshold' was lower than the minumum limit! 'secure-kick-threshold' has been set to the default value (1).");
						secureThreshold = 1;
					}
				} catch (NumberFormatException e) {
					logger.warning(PLUG_NAME + "Failed to parse Configuration option 'secure-kick-threshold': was not valid integer.");
				}
				
			// Set Kick Message
			} else if (line.contains("secure-kick-message:")) {
				modulus = line.replace("secure-kick-message:", "");
				modulus = modulus.trim();
				secureKickMsg = modulus;
				
			// Set Ban Message
			} else if (line.contains("ban-message:")) {
				modulus = line.replace("ban-message:", "");
				modulus = modulus.trim();
				banMessage = modulus;
				
			// Are we using MySQL?
			} else if (line.contains("use-mysql:")) {
				modulus = line.replace("use-mysql:", "");
				modulus = modulus.trim();
				if (modulus.equalsIgnoreCase("true")) {
					usingMySQL = true;
				} else if (modulus.equalsIgnoreCase("false")){
					usingMySQL = false;
				} else {
					logger.severe(PLUG_NAME + COE1 + "use-mysql" + COE2);
				}
			
			// If yes, what is the DB address?
			} else if (line.contains("database-address:") && usingMySQL) {
				modulus = line.replace("database-address:", "");
				modulus = modulus.trim();
				if (!modulus.isEmpty()) {
					mySQLdb = modulus;
				} else {
					logger.severe(PLUG_NAME + "No address given for MySQL Database!");
				}
				
			// What is the username?
			} else if (line.contains("user-name:") && usingMySQL) {
				modulus = line.replace("user-name:", "");
				modulus = modulus.trim();
				if (!modulus.isEmpty()) {
					mySQLuser = modulus;
				} else {
					logger.severe(PLUG_NAME + "No user name specified for MySQL access!");
				}
				
			// What is the password?
			} else if (line.contains("password:") && usingMySQL) {
				modulus = line.replace("password:", "");
				modulus = modulus.trim();
				if (!modulus.isEmpty()) {
					mySQLpassword = modulus;
				} else {
					logger.severe(PLUG_NAME + "No password given for MySQL Database!");
				}
			}
		}
	}
	
	// Returns list of exempt players from Configuration File
	public static ArrayList<String> getPlayerExemptList() {
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
	
	public static ArrayList<String> getIpExemptList() {
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
			} else if (!notifyLogin){
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

    private static void createDefaultDirectory() {
        if (!pluginPath.exists())
        pluginPath.mkdir();
    }
}
