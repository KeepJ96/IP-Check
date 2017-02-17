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
import net.risenphoenix.ipcheck.database.DatabaseController;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdPurge extends Command {

    private DatabaseController db;

    public CmdPurge(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        this.db = IPCheck.getInstance().getDatabaseController();

        setName(getLocalString("CMD_PURGE"));
        setHelp(getLocalString("HELP_PURGE"));
        setSyntax("ipc purge <PLAYER | IP>");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.purge")
        });
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Regex for differentiating between IP and username
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

        // If the argument is an IP-Address, validate it
        if (args[1].matches(ip_filter)) {
            if (db.isValidIP(args[1])) {
                // Purge the IP and send success message.
                db.purgeIP(args[1]);
                sendPlayerMessage(sender, String.format(
                        getLocalString("PURGE_SUC"), args[1]));
            } else {
                // Report if the IP was not found in the database.
                sendPlayerMessage(sender, getLocalString("NO_FIND"));
            }

        // If the argument is a Player, validate it
        } else {
            if (db.isValidPlayer(args[1])) {
                // Purge the player and send success message.
                db.purgePlayer(args[1]);
                sendPlayerMessage(sender, String.format(
                        getLocalString("PURGE_SUC"), args[1]));
            } else {
                // Report if the Player was not found in the database.
                sendPlayerMessage(sender, getLocalString("NO_FIND"));
            }
        }
    }
}
