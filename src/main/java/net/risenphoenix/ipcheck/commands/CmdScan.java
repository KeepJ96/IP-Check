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
import net.risenphoenix.ipcheck.objects.IPObject;
import net.risenphoenix.ipcheck.objects.UserObject;
import net.risenphoenix.ipcheck.util.ListFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public class CmdScan extends Command {

    private IPCheck ipc;

    public CmdScan(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        // Initialize IPC variable
        this.ipc = IPCheck.getInstance();

        setName(this.getLocalString("CMD_SCAN"));
        setHelp(this.getLocalString("HELP_SCAN"));
        setSyntax("ipc scan");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.scan")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Method Variables
        Player[] online = ipc.getOnlinePlayers();
        ArrayList<Player> detected = new ArrayList<Player>();

        // Loop through online players
        for (Player p : online) {
            // Create Store for Unique Accounts linked to this player
            ArrayList<String> unique_names = new ArrayList<String>();

            // Fetch User Object for this player
            UserObject user = ipc.getDatabaseController()
                    .getUserObject(p.getName());

            // If no IPs were found for this user, skip them and continue
            if (user.getNumberOfIPs() == 0) continue;

            // Fetch IP Objects from User Object
            ArrayList<IPObject> ipos = new ArrayList<IPObject>();

            for (String ip : user.getIPs()) {
                ipos.add(ipc.getDatabaseController().getIPObject(ip));
            }

            // Get Unique Accounts from IP Objects
            for (IPObject ipo : ipos) {

                /* If IP only has one linked user and that user is the name of
                 * the player we're currently checking, skip it. */
                if (ipo.getNumberOfUsers() == 1) {
                    if (ipo.getUsers().contains(p.getName()
                            .toLowerCase())) continue;
                }

                // Log Unique Account Names to the Storage ArrayList
                for (String s : ipo.getUsers()) {
                    if (!s.equalsIgnoreCase(p.getName())) {
                        if (!unique_names.contains(s.toLowerCase())) {
                            unique_names.add(s.toLowerCase());
                        }
                    }
                }
            }

            /* If multiple accounts were found for the user, add them to the
             * detection queue */
            if (unique_names.size() > 0) detected.add(p);
        }

        // Output Results to Sender
        if (detected.size() > 0) {
            // Convert ArrayList to type String for use with ListFormatter
            ArrayList<String> convert = new ArrayList<String>();
            for (Player p : detected) convert.add(p.getName());

            // Output Header
            this.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
            this.sendPlayerMessage(sender, ChatColor.RED +
                    this.getLocalString("SCAN_TITLE"));
            this.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);

            // Fetch Formatted List
            StringBuilder list = new ListFormatter(convert).getFormattedList();

            // Display Results
            this.sendPlayerMessage(sender, list.toString(), false);
            this.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
        } else {
            this.sendPlayerMessage(sender, this.getLocalString("SCAN_CLEAN"));
        }
    }
}
