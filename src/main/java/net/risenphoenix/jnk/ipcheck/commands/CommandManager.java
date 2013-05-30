/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.risenphoenix.jnk.ipcheck.commands;

import java.util.ArrayList;
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
    private IpcCommand Check = new CmdCheck();
    private IpcCommand Ban = new CmdBan();
    private IpcCommand Unban = new CmdUnban();
    private IpcCommand Exempt = new CmdExempt();
    private IpcCommand Unexempt = new CmdUnexempt();
    private IpcCommand Purge = new CmdPurge();
    private IpcCommand Toggle = new CmdToggle();
    private IpcCommand ExemptListAll = new CmdExemptListAll();
    private IpcCommand ExemptListIp = new CmdExemptListIp();
    private IpcCommand ExemptListPlayer = new CmdExemptListPlayer();
    private IpcCommand Reload = new CmdReload();
    private IpcCommand About = new CmdAbout();
    private IpcCommand Help = new CmdHelp();
    private IpcCommand Convert = new CmdConvert();
    private IpcCommand Kick = new CmdKick();
    private IpcCommand SBan = new CmdSBan();
    
    public ArrayList<IpcCommand> getAllCommands(){
        //Change the order to change the order in the /help command
        ArrayList<IpcCommand> cmds = new ArrayList<IpcCommand>();
        cmds.add(Check);
        cmds.add(Kick);
        cmds.add(SBan);
        cmds.add(Ban);
        cmds.add(Unban);
        cmds.add(Exempt);
        cmds.add(Unexempt);
        cmds.add(ExemptListAll);
        cmds.add(ExemptListIp);
        cmds.add(ExemptListPlayer);
        cmds.add(Toggle);
        cmds.add(Purge);
        cmds.add(Convert);
        cmds.add(About);
        cmds.add(Reload);
        //cmds.add(Help);
        return cmds;
    }
    
    /*** Returns the String Identifier of the Command to be called.*/
    public IpcCommand executeCommand(String[] args,CommandSender sender) {
            if (args.length > 0) {
                    // Ban Command
                    if (args[0].equalsIgnoreCase("ban")) return Ban;

                    // Unban Command
                    if (args[0].equalsIgnoreCase("unban")) return Unban;

                    // Exempt Command
                    if (args[0].equalsIgnoreCase("exempt")) {
                        if (args.length==2){
                                return Exempt;
                        }else{
                            sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + "An invalid sub-command or no sub-command was specified.");
                            return null;
                        }
                    } 

                     // Unexempt Command
                    if (args[0].equalsIgnoreCase("unexempt")) return Unexempt;

                    // Purge Command
                    if (args[0].equalsIgnoreCase("purge")) return Purge;

                    // Toggle Command
                    if (args[0].equalsIgnoreCase("toggle")) return Toggle;

                    // Exempt-List Command
                    if (args[0].equalsIgnoreCase("exempt-list")) {
                            if (args.length > 1) {
                                    if (args[1].equalsIgnoreCase("ip")) {
                                            return ExemptListIp;
                                    } else if (args[1].equalsIgnoreCase("player")) {
                                            return ExemptListPlayer;
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + "An invalid sub-command or no sub-command was specified.");
                                        return null;
                                    }
                            } else {
                                    return ExemptListAll;
                            }
                    }

                    // Reload Command
                    if (args[0].equalsIgnoreCase("reload")) return Reload;

                    // About Command
                    if (args[0].equalsIgnoreCase("about")) return About;

                    // Help Command
                    if (args[0].equalsIgnoreCase("help")) return Help;

                    // Convert Command
                    if (args[0].equalsIgnoreCase("convert")) return Convert;

                    // Kick Command
                    if (args[0].equalsIgnoreCase("kick")) return Kick;

                    // SBan Command
                    if (args[0].equalsIgnoreCase("sban")) return SBan;


                    if (args.length < 2) return Check; // If it was not one of the above commands and arguments is greater than 0, then pass it to the default check command.
            } else {
                    return null; // No Command was Given.
            }
            sender.sendMessage(ChatColor.GOLD + TranslationManager.PLUG_NAME + ChatColor.YELLOW + "An invalid command was specified.");
            return null;        
    }
}
