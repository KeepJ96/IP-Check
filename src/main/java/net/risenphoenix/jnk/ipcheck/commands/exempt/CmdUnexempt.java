package net.risenphoenix.jnk.ipcheck.commands.exempt;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.risenphoenix.jnk.ipcheck.configuration.ConfigurationManager;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;

public class CmdUnexempt implements IpcCommand{

	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.hasPermission("ipcheck.unexempt") || sender.isOp()) {
			if (args.length == 3) {
                                boolean result=false;
                                if(args[2].contains(".")){
                                    result = IPcheck.Instance.Database.unexemptIP(args[2]);
                                }else{
                                    result = IPcheck.Instance.Database.unexemptPlayer(args[2]);
                                }
				if (result) {
					sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.Instance.Translation.getTranslation("EXEMPTION_DEL_SUC"));
				} else {
					sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + IPcheck.Instance.Translation.getTranslation("EXEMPTION_DEL_ERR"));
				}
			} else {
				sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NUM_ARGS_ERR"));
			}
		} else {
			sender.sendMessage(IPcheck.Instance.Translation.getTranslation("NO_PERM_ERR"));
		}
	}

	@Override
	public String getHelp() {
		return "Removes the specified exemption from file.";
	}

	@Override
	public String getSyntax() {
		return "unexempt <player||IP-address>";
	}

	@Override
	public Permission[] getPermissions() {
		Permission perms[] = {
			new Permission("ipcheck.unexempt")
		};
		
		return perms;
	}

	@Override
	public String getName() {
		return "Unexempt";
	}

}
