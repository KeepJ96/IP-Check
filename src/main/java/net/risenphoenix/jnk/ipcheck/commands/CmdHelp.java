package net.risenphoenix.jnk.ipcheck.commands;

import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdHelp implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "=================[IP-Check]================");
		
		int arg = 1;
		
		if (args.length > 1) {
			try {
				arg = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) { }
		}
		
		int[] bounds = getBounds(arg);
		
		for(int i = bounds[0]; i < bounds[1]; i++) {
			for(IpcCommand ic:IPcheck.commands) {
				if (ic.getID() == i) {
					boolean show = true;
					Permission[] perms = ic.getPermissions();
					
					for(int ii = 0; i < perms.length; i++) {
						if (!sender.hasPermission(perms[ii])) {
							show = false;
							break;
						}
					}
					
					if (show) {
						sender.sendMessage(ChatColor.GREEN + ic.getName() + ":");
						sender.sendMessage(ChatColor.YELLOW + " " + ic.getHelp());
						sender.sendMessage(ChatColor.RED + "    " + "Syntax:" + ChatColor.LIGHT_PURPLE + " /c " + ic.getSyntax());
						if (i < bounds[1]) {
							sender.sendMessage(ChatColor.GOLD + "------------------------------------------");
						}
					}
				}
			}
		}
		
		sender.sendMessage(ChatColor.RED + "Type " + ChatColor.YELLOW + "/c help " + (arg + 1) + ChatColor.RED + " to see more help.");
		sender.sendMessage(ChatColor.GOLD + "==========================================");
	}

	@Override
	public int getID() {
		return 12;
	}

	@Override
	public String getHelp() {
		return "Provides information about all of the associated IP-Check Commands.";
	}

	@Override
	public String getSyntax() {
		return "help";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.use")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Help";
	}
	
	// Command Specific
	private int[] getBounds(int arg) {
		int[] bounds = new int[2];
		
		int tick = arg;
		int start = -4;
		int end = 0;
		
		if (arg <= 1) {
			tick = 1;
		}
		
		if (arg > ((IPcheck.commands.size() / 4) + 1)) {
			tick = (IPcheck.commands.size() / 4) + 1;
		}
		
		// Create Start Value
		for (int i = 0; i < tick; i++) {
			start += 4;
		}
		
		// Create End Value
		end = start + 4;
		
		bounds[0] = start;
		bounds[1] = end;
		
		return bounds;
	}

}
