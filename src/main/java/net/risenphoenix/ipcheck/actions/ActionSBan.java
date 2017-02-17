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

import net.risenphoenix.commons.configuration.ConfigurationManager;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.objects.UserObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ActionSBan {

    private DatabaseController db;
    private ConfigurationManager config;

    private String input;
    private String message;

    public ActionSBan(final IPCheck ipcheck, String input, String message) {
        this.db = ipcheck.getDatabaseController();
        this.config = ipcheck.getConfigurationManager();

        this.input = input;
        this.message = message;
    }

    public Object[] execute() {
        // Store Ban Message
        String banMsg = (message == null || message.length() <= 0) ?
                config.getString("ban-message") : message;

        // Fetch UserObject
        UserObject upo = db.getUserObject(input);

        // If this UPO is already banned, return a 0 value.
        if (upo.getBannedStatus()) {
            return new Object[]{0, input};
        }

        // Fetch Offline Player and ban
        OfflinePlayer offline = Bukkit.getOfflinePlayer(input);
        offline.setBanned(true);

        // Ban Player in Database
        db.banPlayer(input, banMsg);

        // Fetch Online Player Object
        Player banPlayer = Bukkit.getPlayer(input);

        if (banPlayer != null) {
            banPlayer.kickPlayer(banMsg);
        }

        // Return value
        return new Object[] {1, input};
    }
}
