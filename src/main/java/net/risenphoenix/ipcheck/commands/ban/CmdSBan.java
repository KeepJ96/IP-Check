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

package net.risenphoenix.ipcheck.commands.ban;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.actions.ActionBroadcast;
import net.risenphoenix.ipcheck.actions.ActionSBan;
import net.risenphoenix.ipcheck.util.MessageParser;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdSBan extends Command {

    public CmdSBan(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_SBAN"));
        setHelp(getLocalString("HELP_SBAN"));
        setSyntax("ipc sban <PLAYER> [MESSAGE]");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.ban")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Does the Configuration Allow this command to execute?
        if (!this.getPlugin().getConfigurationManager()
                .getBoolean("should-manage-bans")) {
            this.sendPlayerMessage(sender, this.getLocalString("DISABLE_ERR"));
            return;
        }

        // Parse Ban Message if one exists
        String message = new MessageParser(args, 2).parseMessage();

        // Confirm Ban Message is not Empty
        if (message == null || message.length() <= 0) message =
                IPCheck.getInstance().getConfigurationManager()
                        .getString("ban-message");

        // Regex Filter for identifying IPs
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

        // Check for IPs. If the argument is an IP, abort the command.
        if (args[1].matches(ip_filter)) {
            sendPlayerMessage(sender, getLocalString("SBAN_IP_HELP"));
            return;
        }

        // Execute Ban and get Results
        Object[] results = new ActionSBan(IPCheck.getInstance(), args[1],
                message).execute();

        // Fetch Modified Count
        int count = (Integer) results[0];

        // Stats Link
        IPCheck.getInstance().getStatisticsObject().logPlayerBan(count);

        // If there were no modified accounts, return NO_MODIFY message
        if (count == 0) {
            sendPlayerMessage(sender, getLocalString("NO_MODIFY"));
            return;
        }

        // Set up Broadcast Notification
        ActionBroadcast ab;
        String broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.RED +
                "%s" + ChatColor.GOLD + " was banned by " +
                ChatColor.GREEN + "%s" + ChatColor.GOLD + " for: %s";

        // Display Broadcast
        ab = new ActionBroadcast(broadcastMsg, new String[]{
                results[1].toString(), sender.getName(), message},
                new Permission[]{new Permission("ipcheck.seeban")}, false);

        // Execute Broadcast
        ab.execute();
    }
}