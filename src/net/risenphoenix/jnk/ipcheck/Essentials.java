package net.risenphoenix.jnk.ipcheck;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Essentials implements Backend{
	
	// BACKEND MANAGER FOR ESSENTIALS
	
	private static ArrayList<String> playerInfo = new ArrayList<String>();
	private static Logger logger = Bukkit.getLogger();
	
	// Messages
	private static final String PLUG_NAME = "[IP-Check] ";
	private static final String BAN_LIST_READ_ERR = "Error occurred while attempting to read banned-ips.txt!";
	private static final String PLAYER_FILE_READ_ERR = "Error occurred while attempting to read essentials player file!";
	private static final String INIT_BACKEND = "Initizalizing Essentials Backend...";
	
	// Files
	private static File bannedIPs = new File("banned-ips.txt");
	private static File playersDir = new File("plugins/Essentials/userdata");
	
	@Override
	public void onLoad() {
		double initTime = System.currentTimeMillis();
		logger.info(PLUG_NAME + INIT_BACKEND);
		loadFile();
		double haltTime = System.currentTimeMillis();
		logger.info(PLUG_NAME + "Initialization complete! Time taken: " + ((haltTime - initTime) / 1000) + " seconds.");
	}

	@Override
	public void onDisable() {
		logger.info(PLUG_NAME + "Backend Manager successfully shutdown!");
	}

	@Override
	public void loadFile() {
		playerInfo.clear();
		
		File path = null;
		File[] playerFiles = playersDir.listFiles(new FilenameFilter() {
			@Override
		    public boolean accept(File path, String name) {
				if (name.toLowerCase().endsWith(".yml") && !name.toLowerCase().startsWith("nation_") && !name.toLowerCase().startsWith("town_")) {
					return true; 
				} else {
					return false;
				}
		    }
		});
		
		for (int i = 0; i < playerFiles.length; i++) {
			path = new File("" + playerFiles[i]);
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
			
			String player = playerFiles[i].toString();
			player = player.replace(playersDir.toString(), "");
			player = player.replace("" + player.charAt(0), "");
			sb.append(player.replace(".yml", ""));
			sb.append("|");
			
			try {
				FileInputStream fstream = new FileInputStream(path);
				DataInputStream in = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				while ((strLine = br.readLine()) != null) {
					if (strLine.startsWith("ipAddress: ")) {
						strLine = strLine.replace("ipAddress: ", "");
						sb.append(strLine);
					}
				}
			} catch (IOException e) {
				logger.severe(PLAYER_FILE_READ_ERR);
			} finally {
				try {
					if (br != null) {
						br.close();
					}
				} catch (Exception e) {
					logger.severe(e.getMessage());
				}
			}
			
			playerInfo.add(sb.toString().toLowerCase());
		}
	}

	@Override
	public void saveFile() {
		// UNUSED. HANDLED BY ESSENTIALS.
	}

	@Override
	public void log(String player, String ip) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		
		sb.append(player);
		sb.append("|");
		sb.append(ip);
		
		// Check if player is already in the list.
		for (String s:playerInfo) {
			if (playerInfo.contains(sb.toString().toLowerCase())) {
				return;
			} else if (s.contains(player.toLowerCase())) {
				playerInfo.remove(index);
				playerInfo.add(sb.toString());
				return;
			}
			
			index++;
		}
		
		playerInfo.add(sb.toString().toLowerCase());
	}

	@Override
	public void generateFile() {
		// UNUSED. HANDLED BY ESSENTIALS.
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
		
		// First look for exact name
		for(String s:playerInfo) {
			StringBuilder name = new StringBuilder();
			int indexName = 0;
			int index = 0;
			
			while (s.charAt(indexName) != '|') {
				name.append(s.charAt(indexName));
				indexName++;
			}
			
			if (name.toString().toLowerCase().equals(player.toLowerCase())) {
				while (s.charAt(index) != '|') {
					index++;
				}
				
				index++; // Skip the '|' character
				
				while (index < s.length()) {
					ip.append(s.charAt(index));
					index++;
				}
				
				return ip.toString();
			}
		}
		
		// If the exact name could not be found
		for(String s:playerInfo) {
			int index = 0;
			
			if (s.toLowerCase().contains(player.toLowerCase())) {
				while (s.charAt(index) != '|') {
					index++;
				}
				
				index++;
				
				while (index < s.length()) {
					ip.append(s.charAt(index));
					index++;
				}
				
				return ip.toString();
			}
		}
		
		// If neither loop returned a match, then return.
		return "no-find";
	}

	@Override
	public OfflinePlayer getPlayer(String arg, ArrayList<String> alts) {
		String getPlayer = null;
		
		// Check for a match between the argument given and the list of alts returned from the IP
		for (String s:alts) {
			if (s.toLowerCase().contains(arg.toLowerCase())) {
				getPlayer = s;
				break;
			}
		}
		
		// If, for whatever reason, the string is still null after the above, then set it equal to the first element in alts
		if (getPlayer == null) {
			getPlayer = alts.get(0);
		}
		
		// return player object
		return Bukkit.getOfflinePlayer(getPlayer);
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
