package net.risenphoenix.jnk.ipcheck.backend;

import java.io.File;
import java.util.ArrayList;


/***
 * Main Interface for backend management. Contains all method declarations required by any and all Backend Managers for IP-Check.
 */
public interface Backend {
	
	/***
	 * Performs any specified instructions required by the Backend Manager when IP-Check starts.
	 */
	public void onLoad();
	
	/***
	 * Performs any specified instructions required by the Backend Manager when IP-Check shuts down.
	 */
	public void onDisable();
	
	/*** Returns the Registrar (Call-name) of the backend Manager in Use
	 * @return Backend Manager Registrar String
	 */
	public String getRegistrar();
	
	/***
	 * Loads Save File from hard-disk.
	 * @return save file as ArrayList.
	 */
	//public ArrayList<String> loadFile(File file);
	
	/***
	 * Writes a User-Name/IP entry to an active copy of the Backend Manager's save-file.
	 * @param player
	 * @param ip
	 */
	public void log(String player, String ip);
	
	/***
	 * Generates a blank save file on disk for use with the Backend Manager.
	 */
	public void initializeBackend();
	
	/***
	 * Scans the database to return a list of all accounts associated with the IP Specified
	 * @param player
	 * @return ArrayList of player accounts.
	 */
	public ArrayList<String> getAlts(String ip);
	
	/***
	 * Checks if IP given is banned or not.
	 * @return true if IP is banned, return false if IP is not banned.
	 */
	//public boolean isBannedIP(String ip);
	
        /***
	 * Returns the last known IP address of the player name given, if one exists.
	 * @param player
	 * @return IP address.
	 */
        public String getLastKnownIP(String player);
        
	/***
	 * Returns the IP address of the player name given, if one exists.
	 * @param player
	 * @return IP addresses.
	 */
	public ArrayList<String> getIPs(String player);
	
	/***
	 * Returns the Player Object based on the closest match between @arg and @alts, if one exists, otherwise returns the player object for the first entry in @alts.
	 * @param
	 * @return Player object.
	 */
	//public OfflinePlayer getPlayer(String arg, ArrayList<String> alts);
	
	/***
	 * Checks if IP address given exists or not.
	 * @param ip
	 * @return IP address or "no-find" if the IP address was not found.
	 */
	public String checkIPaddress(String ip);
}
