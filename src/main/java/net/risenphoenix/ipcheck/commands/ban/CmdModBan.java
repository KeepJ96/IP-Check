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
import net.risenphoenix.ipcheck.objects.UserObject;
import net.risenphoenix.ipcheck.util.MessageParser;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdModBan extends Command {

    public CmdModBan(Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_MODBAN"));
        setHelp(getLocalString("HELP_MODBAN"));
        setSyntax("ipc modban <PLAYER> <MESSAGE>");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.banmodify")
        });
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Must be a message specified.
        if (args.length <= 2) {
            sendPlayerMessage(sender, getLocalString("NUM_ARGS_ERR"));
            return;
        }

        // Store IP differential regex
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

        if (args[1].matches(ip_filter)) {
            sendPlayerMessage(sender, getLocalString("MODBAN_IP"));
            return;
        } else {
            String msg = new MessageParser(args, 2).parseMessage();
            UserObject upo = IPCheck.getInstance().getDatabaseController()
                    .getUserObject(args[1]);

            if (upo != null) {
                if (upo.getBannedStatus()) {
                    IPCheck.getInstance().getDatabaseController()
                            .banPlayer(args[1], msg);

                    sendPlayerMessage(sender, getLocalString("MODBAN_SUC"));
                } else {
                    sendPlayerMessage(sender, getLocalString("NO_MODIFY"));
                }
            } else {
                sendPlayerMessage(sender, getLocalString("NO_FIND"));
            }
        }
    }

}
