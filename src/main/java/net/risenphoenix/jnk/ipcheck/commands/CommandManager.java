package net.risenphoenix.jnk.ipcheck.commands;

import java.util.ArrayList;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.translation.TranslationManager;
import net.risenphoenix.jnk.ipcheck.commands.CmdAbout;
import net.risenphoenix.jnk.ipcheck.commands.CmdCheck;
import net.risenphoenix.jnk.ipcheck.commands.CmdConvert;
import net.risenphoenix.jnk.ipcheck.commands.CmdHelp;
import net.risenphoenix.jnk.ipcheck.commands.CmdKick;
import net.risenphoenix.jnk.ipcheck.commands.CmdPurge;
import net.risenphoenix.jnk.ipcheck.commands.CmdReload;
import net.risenphoenix.jnk.ipcheck.commands.CmdToggle;
import net.risenphoenix.jnk.ipcheck.commands.IpcCommand;
import net.risenphoenix.jnk.ipcheck.commands.ban.CmdBan;
import net.risenphoenix.jnk.ipcheck.commands.ban.CmdSBan;
import net.risenphoenix.jnk.ipcheck.commands.ban.CmdUnban;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdExempt;
import net.risenphoenix.jnk.ipcheck.commands.exempt.CmdUnexempt;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExemptListAll;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExemptListIp;
import net.risenphoenix.jnk.ipcheck.commands.exempt.list.CmdExemptListPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author FR34KYN01535
 */
public class CommandManager {
    public CommandManager(){
        //
    }
    private IpcCommand CheckCommand = new CmdCheck();
    private IpcCommand BanCommand = new CmdBan();
    private IpcCommand UnbanCommand = new CmdUnban();
    private IpcCommand ExemptCommand = new CmdExempt();
    private IpcCommand UnexemptCommand = new CmdUnexempt();
    private IpcCommand PurgeCommand = new CmdPurge();
    private IpcCommand ToggleCommand = new CmdToggle();
    private IpcCommand ExemptListAllCommand = new CmdExemptListAll();
    private IpcCommand ExemptListIpCommand = new CmdExemptListIp();
    private IpcCommand ExemptListPlayerCommand = new CmdExemptListPlayer();
    private IpcCommand ReloadCommand = new CmdReload();
    private IpcCommand AboutCommand = new CmdAbout();
    private IpcCommand HelpCommand = new CmdHelp();
    private IpcCommand ConvertCommand = new CmdConvert();
    private IpcCommand KickCommand = new CmdKick();
    private IpcCommand SBanCommand = new CmdSBan();
    
    public ArrayList<IpcCommand> getAllCommands(){
        //Change the order to change the order in the /help command
        ArrayList<IpcCommand> cmds = new ArrayList<IpcCommand>();
        cmds.add(CheckCommand);
        cmds.add(KickCommand);
        cmds.add(SBanCommand);
        cmds.add(BanCommand);
        cmds.add(UnbanCommand);
        cmds.add(ExemptCommand);
        cmds.add(UnexemptCommand);
        cmds.add(ExemptListAllCommand);
        cmds.add(ExemptListIpCommand);
        cmds.add(ExemptListPlayerCommand);
        cmds.add(ToggleCommand);
        cmds.add(PurgeCommand);
        cmds.add(ConvertCommand);
        cmds.add(AboutCommand);
        cmds.add(ReloadCommand);
        //cmds.add(Help);
        return cmds;
    }
    
    /*** Returns the Command to be called.*/
    public IpcCommand executeCommand(String[] args,CommandSender sender) {
            if (args.length > 0) {
                    // Ban Command
                    if (args[0].equalsIgnoreCase("ban")) return BanCommand;

                    // Unban Command
                    if (args[0].equalsIgnoreCase("unban")) return UnbanCommand;

                    // Exempt Command
                    if (args[0].equalsIgnoreCase("exempt")) {
                        if (args.length==2){
                                return ExemptCommand;
                        }else{
                            sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "An invalid sub-command or no sub-command was specified.");
                            return null;
                        }
                    } 

                     // Unexempt Command
                    if (args[0].equalsIgnoreCase("unexempt")) return UnexemptCommand;

                    // Purge Command
                    if (args[0].equalsIgnoreCase("purge")) return PurgeCommand;

                    // Toggle Command
                    if (args[0].equalsIgnoreCase("toggle")) return ToggleCommand;

                    // Exempt-List Command
                    if (args[0].equalsIgnoreCase("exempt-list")) {
                            if (args.length > 1) {
                                    if (args[1].equalsIgnoreCase("ip")) {
                                            return ExemptListIpCommand;
                                    } else if (args[1].equalsIgnoreCase("player")) {
                                            return ExemptListPlayerCommand;
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "An invalid sub-command or no sub-command was specified.");
                                        return null;
                                    }
                            } else {
                                    return ExemptListAllCommand;
                            }
                    }

                    // Reload Command
                    if (args[0].equalsIgnoreCase("reload")) return ReloadCommand;

                    // About Command
                    if (args[0].equalsIgnoreCase("about")) return AboutCommand;

                    // Help Command
                    if (args[0].equalsIgnoreCase("help")) return HelpCommand;

                    // Convert Command
                    if (args[0].equalsIgnoreCase("convert")) return ConvertCommand;

                    // Kick Command
                    if (args[0].equalsIgnoreCase("kick")) return KickCommand;

                    // SBan Command
                    if (args[0].equalsIgnoreCase("sban")) return SBanCommand;


                    if (args.length < 2) return CheckCommand; // If it was not one of the above commands and arguments is greater than 0, then pass it to the default check command.
            } else {
                    return null; // No Command was Given.
            }
            sender.sendMessage(ChatColor.GOLD + IPcheck.PLUG_NAME + ChatColor.YELLOW + "An invalid command was specified.");
            return null;        
    }
}
