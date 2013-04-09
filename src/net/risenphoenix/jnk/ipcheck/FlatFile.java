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
import org.bukkit.OfflinePlayer;

public class FlatFile implements Backend{

	// BACKEND MANAGER FOR FLAT-FILE
	
	private static ArrayList<String> playerInfo = new ArrayList<String>();
	private static Logger logger = Bukkit.getLogger();
	
	// Messages
	private static final String PLUG_NAME = "[IP-Check] ";
	private static final String INIT_BACKEND = "Initizalizing Flat-File Backend Manager...";
	private static final String DEINIT_BACKEND = "Shutting down Backend Manager...";
	private static final String BAN_LIST_READ_ERR = "Error occurred while attempting to read banned-ips.txt!";
	private static final String FLAT_FILE_WRITE_ERR = "An error occurred while attempting to write to the database document.";
	private static final String FLAT_FILE_READ_ERR = "An error occurred while attempting to read the database document.";
	private static final String FLAT_FILE_GEN_ERR = "An error occurred while attempting to generate a new database document!";
	
	// Files
	private static File bannedIPs = new File("banned-ips.txt");
	private static File path = new File("plugins/IP-check/database.db");

	@Override
	public void onLoad() {
		double initTime = System.currentTimeMillis();
		logger.info(PLUG_NAME + INIT_BACKEND);
		generateFile();
		loadFile();
		double haltTime = System.currentTimeMillis();
		logger.info(PLUG_NAME + "Initialization complete! Time taken: " + ((haltTime - initTime) / 1000) + " seconds.");
	}

	@Override
	public void onDisable() {
		double initTime = System.currentTimeMillis();
		logger.info(PLUG_NAME + DEINIT_BACKEND);
		saveFile();
		double haltTime = System.currentTimeMillis();
		logger.info(PLUG_NAME + "Backend Manager successfully shutdown! Time taken: " + ((haltTime - initTime) / 1000) + " seconds.");
	}

	@Override
	public void loadFile() {
		playerInfo.clear(); // To prevent spill-over in the event of an in-game reload.
		BufferedReader br = null;
		
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				playerInfo.add(strLine);
			}
			
		} catch (Exception e) {
			logger.severe(FLAT_FILE_READ_ERR);
			
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
		}
		
	}

	@Override
	public void saveFile() {
		FileWriter f = null;
		
		try {
			if (path.exists()) {
				path.delete();
			}
			
        	f = new FileWriter(path, true);
	        
	        for(String s:playerInfo) {
	        	f.write(s + "\r\n");
	        }
		} catch (Exception e) {
			e.printStackTrace();
	    	logger.severe(FLAT_FILE_WRITE_ERR);
		} finally {
			try {
				if (f != null) {
					f.close();
				}
			} catch (Exception e){
				logger.severe(e.getMessage());
			}
		}
	}

	@Override
	public void log(String player, String ip) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		
		sb.append(player);
		sb.append("|");
		sb.append(ip);
		
		logger.info(sb.toString());
		
		// Check if player is already in the list.
		for (String s:playerInfo) {
			if (playerInfo.contains(sb.toString())) {
				return;
			} else if (s.contains(player)) {
				playerInfo.remove(index);
				playerInfo.add(sb.toString());
				return;
			}
			
			index++;
		}
		
		playerInfo.add(sb.toString());
	}

	@Override
	public void generateFile() {
		if (!path.exists()) {
			FileWriter f = null;
			
			try {
				f = new FileWriter(path, true);
				
			} catch (IOException e) {
				logger.severe(FLAT_FILE_GEN_ERR);
				
			} finally {
				try {
					if (f != null) {
						f.close();
					}
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}
		}
	}

	@Override
	public ArrayList<String> getAlts(String ip) {
		ArrayList<String> players = new ArrayList<String>();
		
		for (String s:playerInfo) {
			if (s.contains(ip)) {
				StringBuilder sb = new StringBuilder();
				int index = 0;
				
				while (s.charAt(index) != '|') {
					sb.append(s.charAt(index));
					index++;
				}
				
				players.add(sb.toString());
			}
		}
		
		return players;
	}

	@Override
	public boolean isBannedIP(String ip) {
		BufferedReader br = null;
		
		try {
			FileInputStream fstream = new FileInputStream(bannedIPs);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			/* Skip the first three lines of the file to prevent the while statement
			*  from terminating prematurely. */
			for (int lineSkip = 0; lineSkip < 3; lineSkip++) br.readLine(); 
			
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains(ip)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.severe(BAN_LIST_READ_ERR);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
		}
		
		return false;
	}

	@Override
	public String getIP(String player) {
		StringBuilder ip = new StringBuilder();
		int index = 0;
		
		for(String s:playerInfo) {
			if (s.toLowerCase().contains(player.toLowerCase())) {
				while (s.charAt(index) != '|') {
					index++;
				}
				
				index++;
				
				while (index < s.length()) {
					ip.append(s.charAt(index));
					index++;
				}
				
				break;
			}
		}
		
		if (ip.length() == 0) {
			return "no-find";
		}
		
		// Return the IP address
		return ip.toString();
	}

	@Override
	public OfflinePlayer getPlayer(String ip) {
		StringBuilder playerName = new StringBuilder();
		
		for (String s:playerInfo) {
			if (s.contains(ip)) {
				int index = 0;
				
				while (s.charAt(index) != '|') {
					playerName.append(s.charAt(index));
					index++;
				}
			}
		}
		
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName.toString());
		
		return player;
	}

	@Override
	public String checkIPaddress(String ip) {
		for (String s:playerInfo) {
			if (s.contains(ip)) {
				return ip;
			}
		}
		
		return "no-find";
	}
}
