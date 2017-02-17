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
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdExemptListPlayer extends Command {

    public CmdExemptListPlayer(final Plugin plugin, String[] callArgs,
                           CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_EXEMPT_LIST_PLAYER"));
        setHelp(getLocalString("HELP_EXEMPT_LIST_PLAYER"));
        setSyntax("ipc exempt-list player");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.list"),
        });
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        CmdExemptListAll cmd = (CmdExemptListAll) getPlugin()
                .getCommandManager().getCommand(
                        getLocalString("CMD_EXEMPT_LIST"));

        // Execute Exempt-List with Control Bit set to PLAYER
        if (cmd != null) {
            cmd.executeList(sender, args, CmdExemptListAll.ListType.PLAYER);
        } else {
            sendPlayerMessage(sender, getLocalString("CMD_FETCH_ERR"));
        }
    }
}
