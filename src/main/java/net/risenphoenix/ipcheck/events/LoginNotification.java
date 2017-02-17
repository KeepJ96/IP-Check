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

package net.risenphoenix.ipcheck.events;

import net.risenphoenix.commons.configuration.ConfigurationManager;
import net.risenphoenix.commons.localization.LocalizationManager;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LoginNotification {

    private IPCheck ipc;
    private DatabaseController db;
    private ConfigurationManager config;
    private LocalizationManager local;

    private Player player;
    private String ip;
    private ArrayList<String> accounts;

    public LoginNotification(IPCheck ipc, Player player, String ip,
                             ArrayList<String> accounts) {
        this.ipc = ipc;
        this.db = ipc.getDatabaseController();
        this.config = ipc.getConfigurationManager();
        this.local = ipc.getLocalizationManager();

        this.player = player;
        this.ip = ip;
        this.accounts = accounts;

        this.execute();
    }

    private void execute() {
        Player[] online = ipc.getOnlinePlayers();
        int threshold = config.getInteger("min-account-notify-threshold");
        int acctNum = accounts.size();

        // If the player has more accounts than the set threshold
        if (acctNum > threshold) {

            // If the player and their IP are both non-exempt
            if (!db.isExemptPlayer(player.getName()) && !db.isExemptIP(ip)) {

                // Stats Link
                ipc.getStatisticsObject().logWarningIssue(1);

                for (Player anOnline : online) {
                    displayReport(anOnline);
                }
            }
        }
    }

    private void displayReport(Player p) {
        if (p.hasPermission("ipcheck.getnotify") || p.isOp()) {
            if (config.getBoolean("descriptive-notice")) {

                // Notification Head
                ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                        "------------------------------------------------",
                        false);
                ipc.sendPlayerMessage(p, "Report for: " +
                        ChatColor.LIGHT_PURPLE + player.getName(), false);
                ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                        "------------------------------------------------",
                        false);

                // Notification Body
                if (p.hasPermission("ipcheck.showip") || p.isOp()) {
                    ipc.sendPlayerMessage(p, "IP Address: " +
                            ChatColor.LIGHT_PURPLE + ip, false);
                }

                ipc.sendPlayerMessage(p, ChatColor.LIGHT_PURPLE +
                        player.getName() + ChatColor.RED +
                        " was found to have " + ChatColor.YELLOW + accounts +
                        ChatColor.RED + " possible alternative accounts. " +
                        "Perform command " + ChatColor.LIGHT_PURPLE + "'/ipc " +
                        player.getDisplayName() + "'" + ChatColor.RED +
                        " for " + "more information.", false);

                ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                        "------------------------------------------------", false);
            } else {

                ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                        "------------------------------------------------", false);

                ipc.sendPlayerMessage(p, ChatColor.RED +
                        local.getLocalString("LOGIN_WARN") + " " +
                        ChatColor.LIGHT_PURPLE + player.getDisplayName() +
                        ChatColor.RED + local.getLocalString("LOGIN_EXPLAIN"));

                ipc.sendPlayerMessage(p, ChatColor.DARK_GRAY +
                        "------------------------------------------------", false);
            }
        }
    }

}
