/*
 * Copyright © 2014 Jacob Keep (Jnk1296). All rights reserved.
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

package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.commons.stores.CommandStore;
import net.risenphoenix.ipcheck.commands.*;
import net.risenphoenix.ipcheck.commands.ban.*;
import net.risenphoenix.ipcheck.commands.block.CmdBlock;
import net.risenphoenix.ipcheck.commands.block.CmdUnblock;
import net.risenphoenix.ipcheck.commands.exempt.*;
import net.risenphoenix.ipcheck.commands.exempt.list.*;
import net.risenphoenix.ipcheck.commands.protect.CmdProtect;
import net.risenphoenix.ipcheck.commands.protect.CmdUnprotect;
import net.risenphoenix.ipcheck.commands.toggle.CmdToggle;

public class CmdStore extends CommandStore {

    public CmdStore(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public void initializeStore() {
        // About Command
        this.add(
                new CmdAbout(plugin, new String[]{"ipc", "about"},
                        CommandType.STATIC));

        // Help Command
        this.add(
                new CmdHelp(plugin, new String[]{"ipc", "help", "VAR_ARG_OPT"},
                        CommandType.VARIABLE));

        // Ban Command
        this.add(
                new CmdBan(plugin, new String[]{"ipc", "ban", "VAR_ARG"},
                        CommandType.DYNAMIC));

        // SBan Command
        this.add(
                new CmdSBan(plugin, new String[]{"ipc", "sban", "VAR_ARG"},
                        CommandType.DYNAMIC));

        // Ban-All Command
        this.add(
                new CmdBanAll(plugin, new String[]{"ipc", "banall", "VAR_ARG",
                        "VAR_ARG"}, CommandType.DYNAMIC));

        // Unban Command
        this.add(
                new CmdUnban(plugin, new String[]{"ipc", "unban", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Unban-All Command
        this.add(
                new CmdUnbanAll(plugin, new String[]{"ipc", "unbanall",
                        "VAR_ARG", "VAR_ARG"}, CommandType.VARIABLE));

        // Mod-Ban Command
        this.add(
                new CmdModBan(plugin, new String[]{"ipc", "modban", "VAR_ARG"},
                        CommandType.DYNAMIC));

        // Kick Command
        this.add(
                new CmdKick(plugin, new String[]{"ipc", "kick", "VAR_ARG"},
                        CommandType.DYNAMIC));

        // Exempt Command
        this.add(
                new CmdExempt(plugin, new String[]{"ipc", "exempt", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Unexempt Command
        this.add(
                new CmdUnexempt(plugin, new String[]{"ipc", "unexempt",
                        "VAR_ARG"}, CommandType.VARIABLE));

        // Exempt-List (IP)
        this.add(
                new CmdExemptListIP(plugin, new String[]{"ipc", "exempt-list",
                        "ip"}, CommandType.STATIC));

        // Exempt-List (Player)
        this.add(
                new CmdExemptListPlayer(plugin, new String[]{"ipc",
                        "exempt-list", "player"}, CommandType.STATIC));

        // Exempt-List (All)
        this.add(
                new CmdExemptListAll(plugin, new String[]{"ipc",
                        "exempt-list"}, CommandType.STATIC));

        // Block Command
        this.add(
                new CmdBlock(plugin, new String[]{"ipc", "block", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Unblock Command
        this.add(
                new CmdUnblock(plugin, new String[]{"ipc", "unblock",
                        "VAR_ARG"}, CommandType.VARIABLE));

        // Protect Command
        this.add(
                new CmdProtect(plugin, new String[]{"ipc", "protect",
                        "VAR_ARG"}, CommandType.VARIABLE));

        // Unprotect Command
        this.add(
                new CmdUnprotect(plugin, new String[]{"ipc", "unprotect",
                        "VAR_ARG"}, CommandType.VARIABLE));

        // Scan Command
        this.add(
                new CmdScan(plugin, new String[]{"ipc", "scan"},
                        CommandType.STATIC));

        // Status Command
        this.add(
                new CmdStatus(plugin, new String[]{"ipc", "status"},
                        CommandType.STATIC));

        // Purge Command
        this.add(
                new CmdPurge(plugin, new String[]{"ipc", "purge", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Toggle Command
        this.add(
                new CmdToggle(plugin, new String[]{"ipc", "toggle", "VAR_ARG"},
                        CommandType.VARIABLE));

        // Reload Command
        this.add(
                new CmdReload(plugin, new String[]{"ipc", "reload"},
                        CommandType.STATIC));

        // ROOT COMMAND
        this.add(
                new CmdCheck(plugin, new String[]{"ipc", "VAR_ARG"},
                        CommandType.VARIABLE));
    }
}
