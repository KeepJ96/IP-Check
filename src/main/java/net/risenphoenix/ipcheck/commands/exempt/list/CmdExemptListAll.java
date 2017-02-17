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

package net.risenphoenix.ipcheck.commands.exempt.list;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.util.ListFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public class CmdExemptListAll extends Command {

    public CmdExemptListAll(final Plugin plugin, String[] callArgs,
                            CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_EXEMPT_LIST"));
        setHelp(getLocalString("HELP_EXEMPT_LIST"));
        setSyntax("ipc exempt-list");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.list"),
                new Permission("ipcheck.showip")
        });
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        executeList(sender, args, ListType.ALL);
    }

    /* Command Instructions moved from onExecute to allow access by the other
     * exempt-list commands, so as to minimize the size of the code base. */
    public void executeList(CommandSender sender, String[] args,
                            ListType type) {
        // Fetch Database Controller from IPCheck Instance
        DatabaseController db = IPCheck.getInstance().getDatabaseController();

        // Fetch ArrayList of Exemptions
        ArrayList<String> ipExempt = db.getIPExemptList();
        ArrayList<String> userExempt = db.getPlayerExemptList();

        // Fetch Formatted List for IP Exemptions
        StringBuilder ip_list = new ListFormatter(ipExempt).getFormattedList();

        // Fetch Formatted List for Player Exemptions
        StringBuilder user_list = new ListFormatter(userExempt)
                .getFormattedList();

        // Output List
        sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // IP Exemptions Tally
        if (type.equals(ListType.ALL) || type.equals(ListType.IP)) {
            sendPlayerMessage(sender, ChatColor.GOLD +
                    getLocalString("EXEMPT_LIST_IP") + " " + ChatColor.RED +
                    ipExempt.size(), false);

            sendPlayerMessage(sender, ip_list.toString(), false);
        }

        if (type.equals(ListType.ALL)) {
            sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
        }

        // Player Exemptions Tally
        if (type.equals(ListType.ALL) || type.equals(ListType.PLAYER)) {
            sendPlayerMessage(sender, ChatColor.GOLD +
                    getLocalString("EXEMPT_LIST_PLAYER") + " " + ChatColor.RED +
                    userExempt.size(), false);

            sendPlayerMessage(sender, user_list.toString(), false);
        }

        sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        if (type.equals(ListType.ALL)) {
            // Total Exemptions Tally
            sendPlayerMessage(sender, ChatColor.GOLD +
                    getLocalString("EXEMPT_LIST_TALLY") + " " + ChatColor.RED +
                    (ipExempt.size() + userExempt.size()), false);

            sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
        }
    }

    public enum ListType {
        PLAYER,
        IP,
        ALL
    }
}