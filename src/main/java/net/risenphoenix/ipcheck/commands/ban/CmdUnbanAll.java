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

import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.commons.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdUnbanAll extends Command {

    public CmdUnbanAll(final Plugin plugin, String[] callArgs,
                       CommandType type) {
        super(plugin, callArgs, type);

        setName(this.getLocalString("CMD_UNBANALL"));
        setHelp(this.getLocalString("HELP_UNBANALL"));
        setSyntax("ipc unbanall <START_TIME> <STOP_TIME | now>");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.unban"),
                new Permission("ipcheck.unbanall")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Fetch Ban-All Command from the Command Manager.
        CmdBanAll cmd = (CmdBanAll) getPlugin().getCommandManager()
                .getCommand(getLocalString("CMD_BANALL"));

        // Execute Ban-All with Control Flag set to false.
        if (cmd != null) {
            cmd.executeBan(sender, args, false);
        } else {
            sendPlayerMessage(sender, getLocalString("CMD_FETCH_ERR"));
        }
    }
}
