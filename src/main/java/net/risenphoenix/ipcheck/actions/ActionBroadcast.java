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

package net.risenphoenix.ipcheck.actions;

import net.risenphoenix.ipcheck.IPCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ActionBroadcast {

    private IPCheck ipc;

    private String message;
    private String[] values = null;
    private Permission[] requiredPerms = null;
    private boolean useName;

    public ActionBroadcast(String message, boolean useName) {
        this.message = message;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public ActionBroadcast(String message, Permission[] perms, boolean useName) {
        this.message = message;
        this.requiredPerms = perms;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public ActionBroadcast(String message, String[] values, boolean useName) {
        this.message = message;
        this.values = values;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public ActionBroadcast(String message, String[] values, Permission[] perms,
                           boolean useName) {
        this.message = message;
        this.values = values;
        this.requiredPerms = perms;
        this.ipc = IPCheck.getInstance();
        this.useName = useName;
    }

    public void execute() {
        // Fetch Online Players
        Player[] players = ipc.getOnlinePlayers();

        // Get Final Message
        String finalMsg = this.formatMessage();

        // Broadcast
        for (Player p : players) {
            // Check if Permissions are required, and if so check for them
            if (requiredPerms != null) {
                if (!hasPermission(p)) continue;
            }

            // Display Message
            this.ipc.sendPlayerMessage(p, finalMsg, this.useName);
        }

        // Send Message to Console
        Bukkit.getConsoleSender().sendMessage(finalMsg);
    }

    private String formatMessage() {
        return String.format(this.message, this.values);
    }

    private boolean hasPermission(Player player) {
        // Check for OP Status
        if (player.isOp()) return true;

        // If not OP, check for permissions
        for (Permission p : this.requiredPerms) {
            if (!player.hasPermission(p)) return false;
        }

        // If the check passed, return true
        return true;
    }
}
