/*
 * Copyright © 2017 Jacob Keep (Jnk1296). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 *
 *  * Neither the name of JuNK Software nor the names of its contributors may 
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.actions.ActionBroadcast;
import net.risenphoenix.ipcheck.actions.ActionKick;
import net.risenphoenix.ipcheck.util.MessageParser;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdKick extends Command {

    public CmdKick(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(this.getLocalString("CMD_KICK"));
        setHelp(this.getLocalString("HELP_KICK"));
        setSyntax("ipc kick <PLAYER | IP> [MESSAGE]");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.kick")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Does the Configuration Allow this command to execute?
        if (!this.getPlugin().getConfigurationManager()
                .getBoolean("should-manage-bans")) {
            this.sendPlayerMessage(sender, this.getLocalString("DISABLE_ERR"));
            return;
        }

        // Parse Kick Message if one exists
        String message = new MessageParser(args, 2).parseMessage();

        // Confirm Kick Message is not empty
        if (message == null || message.length() <= 0) message =
                IPCheck.getInstance().getConfigurationManager()
                        .getString("kick-message");

        // Execute Kick / Get Kicked-Accounts Tally
        Object[] results = new ActionKick(IPCheck.getInstance(), sender,
                args[1], message).execute();

        int count = (Integer) results[0];

        // Stats Link
        IPCheck.getInstance().getStatisticsObject().logKickIssue(count);

        // Set up Broadcast Notification
        ActionBroadcast ab;
        String broadcastMsg;

        if (count == 1) {
            broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.RED + "%s" +
                    ChatColor.GOLD + " was kicked by " + ChatColor.GREEN +
                    "%s" + ChatColor.GOLD + " for: %s";
        } else {
            broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.GREEN + "%s" +
                    ChatColor.GOLD + " kicked " + ChatColor.RED + "%s" +
                    ChatColor.GOLD + " accounts for: " + "%s";
        }

        if (count > 0) {
            if (count == 1) {
                ab = new ActionBroadcast(broadcastMsg, new String[]{
                        results[1].toString(), sender.getName(), message},
                        new Permission[]{new Permission("ipcheck.seekick")},
                        false);

                // Execute Broadcast
                ab.execute();
            } else {
                ab = new ActionBroadcast(broadcastMsg, new String[]{
                        sender.getName(), count + "", message},
                        new Permission[]{new Permission("ipcheck.seekick")},
                        false);

                // Execute Broadcast
                ab.execute();
            }
        }
    }
}
