package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;

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
	
	/***
	 * Loads Save File from hard-disk.
	 * @return save file as ArrayList.
	 */
	public void loadFile();
	
	/***
	 * Writes the save file for the Backend Manager to disk.
	 * @param current
	 */
	public void saveFile();
	
	/***
	 * Writes a User-Name/IP entry to an active copy of the Backend Manager's save-file.
	 * @param player
	 * @param ip
	 */
	public void log(String player, String ip);
	
	/***
	 * Generates a blank save file on disk for use with the Backend Manager.
	 */
	public void generateFile();
	
	/***
	 * Scans the active copy of the Backend Manager's save file in order to detect alternate player accounts
	 * @param player
	 * @return ArrayList of player accounts.
	 */
	public ArrayList<String> getAlts(String ip);
	
	/***
	 * Checks if IP given is banned or not.
	 * @return true if IP is banned, return false if IP is not banned.
	 */
	public boolean isBannedIP(String ip);
	
	/***
	 * Returns the IP address of the player name given, if one exists.
	 * @param player
	 * @return IP address.
	 */
	public String getIP(String player);
	
	/***
	 * Returns the Player Object associated with the IP address given, if one exists.
	 * @param ip
	 * @return Player object.
	 */
	public OfflinePlayer getPlayer(String ip, String arg);
	
	/***
	 * Checks if IP address given exists or not.
	 * @param ip
	 * @return IP address or "no-find" if the IP address was not found.
	 */
	public String checkIPaddress(String ip);
}
