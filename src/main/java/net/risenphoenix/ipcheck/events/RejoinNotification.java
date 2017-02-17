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

package net.risenphoenix.ipcheck.events;

import net.risenphoenix.commons.localization.LocalizationManager;
import net.risenphoenix.ipcheck.IPCheck;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RejoinNotification {

    private IPCheck ipc;
    private LocalizationManager local;
    private Player player;

    public RejoinNotification(IPCheck ipc, Player player) {
        this.ipc = ipc;
        this.local = ipc.getLocalizationManager();
        this.player = player;

        this.execute();
    }

    private void execute() {
        Player[] online = ipc.getOnlinePlayers();

        for (int i = 0; i < online.length; i++) {
            displayWarning(online[i]);
        }
    }

    private void displayWarning(Player p) {
        if (p.hasPermission("ipcheck.getnotify") || p.isOp()) {
            ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                    "---------------------------------------", false);

            ipc.sendPlayerMessage(p, ChatColor.RED +
                    local.getLocalString("REJOIN_WARN") + " " +
                    ChatColor.LIGHT_PURPLE + player.getDisplayName() +
                    ChatColor.RED + local.getLocalString("REJOIN_EXPLAIN"));

            ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                    "---------------------------------------", false);
        }
    }
}
