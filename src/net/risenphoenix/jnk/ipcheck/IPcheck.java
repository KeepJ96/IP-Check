package net.risenphoenix.jnk.ipcheck;

import java.net.InetAddress;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

public class IPcheck extends JavaPlugin implements Listener{
	
	// IP-Check v1.2.0 (REBUILD) by Jnk1296. Designed for checking for and banning multi-accounting players.
	
	public static Backend backend = null;
	
	public static Report report = new Report();
	public static LoginReport loginReport = new LoginReport();
	
	public static final String PLUG_NAME = "[IP-Check] ";
	public static final String BAN_LIST_READ_ERR = "Error occurred while attempting to read banned-ips.txt!";
	public static final String PLAYER_FILE_READ_ERR = "Error occurred while attempting to read player file!";
	public static final String NO_PERM_ERR = "You don't have permission to do that!";
	public static final String NUM_ARGS_ERR = "Incorrect Number of Arguments.";
	public static final String ILL_ARGS_ERR = "Illegal Argument(s) were passed into the command.";
	public static final String NO_FIND = "The player specified could not be found.";
	public static final String NO_RECENT = "You have not searched a player yet.";
	public static final String PLAYER_EXEMPT_SUC = "Player added to exemption list!";
	public static final String IP_EXEMPT_SUC = "IP-Address added to exemption list!";
	public static final String EXEMPTION_FAIL = "Sorry. :( Something went wrong. The exemption could not be added.";
	public static final String TOGGLE_SECURE = "Secure-Mode set to: ";
	public static final String TOGGLE_NOTIFY = "Notify-On-Login set to: ";
	public static final String TOGGLE_DETAIL = "Descriptive-Notify set to: ";
	public static final String TOGGLE_ERR = "An error occurred while attempting to set state of toggle.";
	public static final String EXEMPTION_DEL_SUC = "Exemption successfully removed!";
	public static final String EXEMPTION_DEL_ERR = "Exemption specified does not exist.";
	
	public static boolean shouldCheck = true;
	public static String ipToCheck = "";
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this); // Register the Player Login Listener
		Configuration.onLoad(); // Load the Configuration File
		
		// Determine which backend to use
		if (Configuration.backend == 1) {
			backend = new FlatFile();
		} else {
			backend = new Essentials();
		}
		
		backend.onLoad();
	}
	
	@Override
	public void onDisable() {
		backend.onDisable();
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
			
		// Construct the IP Address String
		InetAddress a = e.getAddress();
		String iNetAddress = a.getHostAddress();
		StringBuilder ip = new StringBuilder();
		for (int i = 0; i < iNetAddress.length(); i++) {
			if ((iNetAddress.charAt(i) >= '0' && iNetAddress.charAt(i) <= '9') || iNetAddress.charAt(i) == '.') {
				ip.append(iNetAddress.charAt(i));
			} else if (iNetAddress.charAt(i) == ':') {
				break;
			}
		}
		
		ipToCheck = ip.toString();
		backend.log(player.getName(), ip.toString());
		shouldCheck = true;
		
		if (Configuration.secureMode) {
			shouldCheck = secureCheck(ip.toString(), e);
		}
		
		return;
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		// Do not perform check on operators or players with the "ipcheck.getnotify permission.
		if (!player.isOp() && !player.hasPermission("ipcheck.getnotify")) {
			if (Configuration.notifyLogin && shouldCheck) {
				int accounts = (backend.getAlts(ipToCheck)).size();
				Player playerCheck = e.getPlayer();
				loginReport.execute(ipToCheck, playerCheck, accounts);
			}
		}
		
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("c")) {
			try {
				if (args[0] != null) {
					// Ban Command
					if (args[0].equalsIgnoreCase("ban")) {
						if (sender.hasPermission("ipcheck.ban") || sender.isOp()) {
							if (args.length == 2) {
								String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
								if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
									// Command Instructions Here
									report.execute(banPlayers(backend.getAlts(args[1]), sender, backend.checkIPaddress(args[1]), true), sender, backend.getIP(args[1]), args[1], false);
								} else {
									report.execute(banPlayers(backend.getAlts(backend.getIP(args[1])), sender, backend.getIP(args[1]), true), sender, backend.getIP(args[1]), args[1], false);
								}
							} else {
								sender.sendMessage(NUM_ARGS_ERR);
							}
						} else {
							sender.sendMessage(NO_PERM_ERR);
						}
						
						return true;
							
					// Unban Command
					} else if (args[0].equalsIgnoreCase("unban")) {
						if (sender.hasPermission("ipcheck.unban") || sender.isOp()) {
							if (args.length == 2) {
								//Command Instructions Here
								String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
								if (args[1].toLowerCase().matches(ip_filter.toLowerCase())) {
									// Command Instructions Here
									report.execute(banPlayers(backend.getAlts(args[1]), sender, backend.checkIPaddress(args[1]), false), sender, backend.getIP(args[1]), args[1], false);
								} else {
									report.execute(banPlayers(backend.getAlts(backend.getIP(args[1])), sender, backend.getIP(args[1]), false), sender, backend.getIP(args[1]), args[1], false);
								}
							} else {
								sender.sendMessage(NUM_ARGS_ERR);
							}
						} else {
							sender.sendMessage(NO_PERM_ERR);
						}
						
						return true;
						
					// Player Command
					} else if (args[0].equalsIgnoreCase("player")) {
						sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + "This command has been depricated. Please use '/check' for all player/ip lookups.");
						
						return true;
						
					// Exempt Command
					} else if (args[0].equalsIgnoreCase("exempt")) {
						if (sender.hasPermission("ipcheck.exempt") || sender.isOp()) {
							if (args.length == 3) {
								//Command Instructions Here
								if (args[1].equalsIgnoreCase("player")) {
									if (Configuration.addExemption(1, args[2])) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + PLAYER_EXEMPT_SUC);
									} else {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + EXEMPTION_FAIL);
									}
								} else if (args[1].equalsIgnoreCase("ip")) {
									if (Configuration.addExemption(0, args[2])) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + IP_EXEMPT_SUC);
									} else {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + EXEMPTION_FAIL);
									}
								} else if (args[1].equalsIgnoreCase("remove")) {
									if (sender.hasPermission("ipcheck.exempt.remove") || sender.isOp()) {
										boolean result = Configuration.deleteExemption(args[2]);
										
										if (result) {
											sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + EXEMPTION_DEL_SUC);
										} else {
											sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + EXEMPTION_DEL_ERR);
										}
									} else {
										sender.sendMessage(NO_PERM_ERR);
									}
								} else {
									sender.sendMessage(ILL_ARGS_ERR);
								}
							} else {
								sender.sendMessage(NUM_ARGS_ERR);
							}
						} else {
							sender.sendMessage(NO_PERM_ERR);
						}
						
						return true;
						
					// Exempt-List Command
					} else if (args[0].equalsIgnoreCase("exempt-list")) {
						if (sender.hasPermission("ipcheck.list")) {
							if (args.length == 2) {
								//Command Instructions Here
								if (args[1].equalsIgnoreCase("player")) {
									ArrayList<String> list = Configuration.getPlayerExemptList();
									
									sender.sendMessage("");
									for (String s:list) {
										sender.sendMessage(s);
									}
									
									sender.sendMessage(ChatColor.YELLOW + "Total players in exemption list: " + ChatColor.LIGHT_PURPLE + list.size());
									sender.sendMessage("");
									
								} else if (args[1].equalsIgnoreCase("ip")) {
									ArrayList<String> list = Configuration.getIpExemptList();
									
									sender.sendMessage("");
									for (String s:list) {
										sender.sendMessage(s);
									}
									
									sender.sendMessage(ChatColor.YELLOW + "Total players in exemption list: " + ChatColor.LIGHT_PURPLE + list.size());
									sender.sendMessage("");
									
								} else if (args[1].equalsIgnoreCase("list")) {
									ArrayList<String> list = Configuration.getPlayerExemptList();
									ArrayList<String> list2 = Configuration.getIpExemptList();
									
									sender.sendMessage("");
									for (String s:list) sender.sendMessage(s);
									sender.sendMessage(ChatColor.GOLD + "------------------------------");
									for (String s:list2) sender.sendMessage(s);
									
									sender.sendMessage(ChatColor.YELLOW + "Total exemptions on file: " + ChatColor.LIGHT_PURPLE + (list.size() + list2.size()));
									sender.sendMessage("");
								}
							} else {
								sender.sendMessage(NUM_ARGS_ERR);
							}
						} else {
							sender.sendMessage(NO_PERM_ERR);
						}
						
						return true;
						
					// Toggle Commands
					} else if (args[0].equalsIgnoreCase("toggle")) {
						if (sender.hasPermission("ipcheck.toggle") || sender.isOp()) {
							if (args.length == 2) {
								if (args[1].equalsIgnoreCase("immediate-mode") || args[1].equalsIgnoreCase("immediate") || args[1].equalsIgnoreCase("im")) {
									int response = Configuration.toggle(0);
									
									if (response == 0) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + TOGGLE_NOTIFY + ChatColor.RED + "False");
									} else if (response == 1) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + TOGGLE_NOTIFY + ChatColor.GREEN + "True");
									} else {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.RED + TOGGLE_ERR);
									}
								} else if (args[1].equalsIgnoreCase("notification-mode") || args[1].equalsIgnoreCase("notification") || args[1].equalsIgnoreCase("notify")) {
									int response = Configuration.toggle(1);
									
									if (response == 0) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + TOGGLE_DETAIL + ChatColor.RED + "False");
									} else if (response == 1) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + TOGGLE_DETAIL + ChatColor.GREEN + "True");
									} else {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.RED + TOGGLE_ERR);
									}
								} else if (args[1].equalsIgnoreCase("secure-mode") || args[1].equalsIgnoreCase("secure")) {
									int response = Configuration.toggle(2);
									
									if (response == 0) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + TOGGLE_SECURE + ChatColor.RED + "False");
									} else if (response == 1) {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + TOGGLE_SECURE + ChatColor.GREEN + "True");
									} else {
										sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.RED + TOGGLE_ERR);
									}
								} else {
									sender.sendMessage(ILL_ARGS_ERR);
								}
							} else {
								sender.sendMessage(NUM_ARGS_ERR);
							}
						} else {
							sender.sendMessage(NO_PERM_ERR);
						}
						
						return true;
						
					// Reload the Configuration
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (sender.hasPermission("ipcheck.reload") || sender.isOp()) {
							if (args.length == 1) {
								Configuration.onLoad();
								backend.onLoad();
							} else {
								sender.sendMessage(NUM_ARGS_ERR);
							}
						} else {
							sender.sendMessage(NO_PERM_ERR);
						}
					
						return true;
						
					// About Command
					} else if (args[0].equalsIgnoreCase("about")) {
						sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + "IP-Check v1.2.3 by Jnk1296.");
						return true;
						
					// All else
					} else if (args.length == 1) {
						String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
						//Command Instructions here
						if (args[0].toLowerCase().matches(ip_filter.toLowerCase())) {
							showReport(args[0], sender, false);
							return true;
						} else {
							showReport(args[0].toLowerCase(), sender, true);
							return true;
						}
					}
					
					return false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				sender.sendMessage(NUM_ARGS_ERR);
			} 
		}
			
		return false;
	}
	
	public void showReport(String input, CommandSender sender, boolean forPlayer) {
		if (forPlayer) {
			report.execute(backend.getAlts(backend.getIP(input)), sender, backend.getIP(input), input, true);
		} else {
			report.execute(backend.getAlts(input), sender, backend.getIP(input), input, false);
		}
	}
	
	public boolean secureCheck(String ip, PlayerLoginEvent e) {
		ArrayList<String> players = backend.getAlts(ip);
		int accounts = players.size();
		Player player = e.getPlayer();
		return secureKick(accounts, player.getName(), e, ip);
	}
	
	public boolean secureKick(int accounts, String player, PlayerLoginEvent e, String ip) {
		// If the player was reported to have more than the secure-threshold # of accounts, then kick (if not exempt).
		if (accounts > Configuration.secureThreshold && !Configuration.isExemptPlayer(player) && !Configuration.isExemptIp(ip)) {
			
			if (player != null) {
				e.setKickMessage(Configuration.secureKickMsg);
				e.setResult(Result.KICK_OTHER);
				return false;
			}
		}
		
		return true;
	}
	
	public ArrayList<String> banPlayers(ArrayList<String> players, CommandSender sender, String ip, boolean banning) {
		// Ban or Unban IP Address
		if (ip.equals("no-find")) {
			sender.sendMessage(ChatColor.GOLD + PLUG_NAME + ChatColor.YELLOW + NO_FIND);
			return players;
		}
		
		if (banning) {
			Bukkit.banIP(ip);
			sender.sendMessage("");
			sender.sendMessage("Banned IP Address: " + ip);
			sender.sendMessage("");
		} else if (!banning) {
			Bukkit.unbanIP(ip);
			sender.sendMessage("");
			sender.sendMessage("Unbanned IP Address: " + ip);
			sender.sendMessage("");
		}
		
		// Ban or Unban Players with corresponding IP Address
		for(String s:players) {
			Bukkit.getOfflinePlayer(s).setBanned(banning);
			
			if (banning) {
				Player player = Bukkit.getPlayer(s);
				
				if (player != null) {
					player.kickPlayer(Configuration.banMessage);
				}
				
				sender.sendMessage("Banned " + s);
			} else if (!banning) {
				sender.sendMessage("Pardoned " + s);
			}
		}
		
		return players;
	}
}
