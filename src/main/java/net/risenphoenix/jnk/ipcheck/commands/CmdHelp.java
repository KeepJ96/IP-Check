package net.risenphoenix.jnk.ipcheck.commands;

import java.util.ArrayList;

import net.risenphoenix.jnk.ipcheck.IPcheck;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdHelp implements IpcCommand{

	ArrayList<IpcCommand> commandList = new ArrayList<IpcCommand>();
	
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "=================[IP-Check]================");
		
		int arg = 1;
		
		if (args.length > 1) {
			try {
				arg = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) { }
		}
		
		// Fetch list of commands which apply to the sender.
		for(int i = 0; i < IPcheck.commands.size(); i++) {
			for(IpcCommand ic:IPcheck.commands) {
				if (ic.getID() == i) {
					boolean show = true;
					Permission[] perms = ic.getPermissions();
					
					for(int ii = 0; ii < perms.length; ii++) {
						if (!sender.hasPermission(perms[ii])) {
							show = false;
							break;
						}
					}
					
					if (show) {
						commandList.add(ic);
					}
				}
			}
		}
		
		int[] bounds = getBounds(arg);
		
		for (int i = bounds[0]; i < bounds[1]; i++) {
			sender.sendMessage(ChatColor.GREEN + commandList.get(i).getName() + ":");
			sender.sendMessage(ChatColor.YELLOW + " " + commandList.get(i).getHelp());
			sender.sendMessage(ChatColor.RED + "    " + "Syntax:" + ChatColor.LIGHT_PURPLE + " /c " + commandList.get(i).getSyntax());
			if (i < bounds[1]) {
				sender.sendMessage(ChatColor.GOLD + "------------------------------------------");
			}
		}
		if(arg<4){
		sender.sendMessage(ChatColor.RED + "Type " + ChatColor.YELLOW + "/c help " + (arg + 1) + ChatColor.RED + " to see more help.");
		sender.sendMessage(ChatColor.GOLD + "==========================================");
                }
		commandList.clear();
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
		
		if (arg > ((commandList.size() / 4) + 1)) {
			tick = (commandList.size() / 4) + 1;
		}
		
		// Create Start Value
		for (int i = 0; i < tick; i++) {
			start += 4;
		}
		
		// Create End Value
		end = start + 4;
		
		if (end > commandList.size()) end = commandList.size();
		
		bounds[0] = start;
		bounds[1] = end;
		
		return bounds;
	}

}
