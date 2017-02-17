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

import net.risenphoenix.commons.configuration.ConfigurationManager;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.actions.ActionBroadcast;
import net.risenphoenix.ipcheck.commands.block.BlockManager;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.objects.DateObject;
import net.risenphoenix.ipcheck.objects.IPObject;
import net.risenphoenix.ipcheck.objects.UserObject;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.logging.Level;

public class PlayerLoginListener {

    private IPCheck ipc;
    private ConfigurationManager config;
    private DatabaseController db;
    private PlayerLoginEvent e;

    private BlockManager cBlockManager = null;

    public PlayerLoginListener(IPCheck ipc, PlayerLoginEvent e) {
        this.ipc = ipc;
        this.config = ipc.getConfigurationManager();
        this.db = ipc.getDatabaseController();
        this.e = e;

        this.cBlockManager = ipc.getBlockManager();

        this.execute();
    }

    public void execute() {
        // Fetch IP Address and Player
        Player player = e.getPlayer();
        String address = e.getAddress().getHostAddress();

        boolean debugAddress = false;

        if (debugAddress)
            ipc.sendConsoleMessage(Level.INFO, "Address Output: " + address);

        // Log Player and IP
        db.log(player.getName(), address);

        // Stats Link
        ipc.getStatisticsObject().logPlayerJoin(1);

        // Ban State Updater to keep Ban Records up-to-date.
        if (player.isBanned()) {
            // To prevent re-banning and to preserve ban messages.
            if (!db.getUserObject(player.getName()).getBannedStatus()) {
                db.banPlayer(player.getName(),
                        config.getString("ban-message"));
            }
        } else {
            if (db.getUserObject(player.getName()).getBannedStatus()) {
                db.unbanPlayer(player.getName());
            }
        }

        /* ACTIVE MODE HOOK */
        /* Check if the IP is banned in the database. If it is, ban the player
         * with the banned address. */
         if (config.getBoolean("active-mode") &&
                 config.getBoolean("should-manage-bans")) {
             if (db.isBannedIP(address)) {
                 if (!db.getUserObject(player.getName()).getBannedStatus()) {
                     player.setBanned(true);
                     db.banPlayer(player.getName(),
                            config.getString("ban-message"));
                 }
            }
         }

        // Check Banned Status and Kick if banned
        if (db.isBannedPlayer(player.getName()) &&
                config.getBoolean("should-manage-bans")) {
            e.setKickMessage(db.getBanMessage(player.getName()));
            e.setResult(Result.KICK_BANNED);

            // Execute a Rejoin Notification if the option allows and the player
            // is not exempt from such notifications.
            if (config.getBoolean("warn-on-rejoin-attempt")) {
                if (!db.isRejoinExemptPlayer(player.getName()) &&
                        !db.isRejoinExemptIP(address)) {
                    new RejoinNotification(ipc, player);
                }
            }
            return;
        }

        // Check if the player's country is blocked. If so, kick them with a
        // corresponding message. (GeoIP Services Hook)
        if (config.getBoolean("use-country-blacklist")) {
            // CBlock will be null if the database is not found.
            if (cBlockManager != null) {
                if (cBlockManager.getStatus()) {
                    // Fetch Country ID for player
                    String countryID = cBlockManager.getCountryID(address);
                    String countryName = cBlockManager.getCountry(address);

                    // If the country is blocked via black list, kick the player.
                    if (cBlockManager.isBlockedCountry(countryID)) {
                        e.setKickMessage(config.getString("blocked-message") +
                                " (" + countryName + ")");
                        e.setResult(Result.KICK_OTHER);
                        return;
                    }
                }
            }
        }

        // If the player was not kicked for having a banned status or a blocked
        // country, check for alt accounts with the database. (Secure Mode Hook)
        boolean shouldCheck = true;

        // Attempt a Secure-Kick if Secure-Mode is enabled
        if (config.getBoolean("secure-mode")) {
            shouldCheck = this.secureKick(e, address);
        }

        // Special Check for Me! :D
        if (player.getName().equals("Jnk1296") && !player.hasPlayedBefore()) {
            ipc.sendPlayerMessage(player, "Daddy! :D");
            ActionBroadcast ab = new ActionBroadcast("Daddy! :D",
                    new Permission[]{new Permission("ipcheck.getnotify")},
                    true);

            ab.execute();
        }

        // Perform a Login Notification
        if (config.getBoolean("notify-on-login") && shouldCheck) {
            if (!player.isOp() && !player.hasPermission("ipcheck.getnotify")) {
                IPObject ipo = db.getIPObject(address);
                ArrayList<String> names = new ArrayList<String>();

                for (String s : ipo.getUsers()) {
                    if (!names.contains(s.toLowerCase())) {
                        names.add(s.toLowerCase());
                    }
                }

                // Execute Login Notification
                new LoginNotification(ipc, player, address, names);
            }
        }

    }

    // Attempt to Kick Player in the event of having too many alt accounts
    private boolean secureKick(PlayerLoginEvent e, String ip) {
        String player = e.getPlayer().getName();
        ArrayList<String> names = this.getUniqueAccounts(player);

        int threshold = config.getInteger("secure-kick-threshold");

        // If the number of accounts is greater than the threshold, and the
        // player-name and IP are both non-exempt, then check if the account
        // may log in.
        if (names.size() > threshold && !db.isExemptPlayer(player) &&
                !db.isExemptIP(ip)) {

            ArrayList<DateObject> dates = new ArrayList<DateObject>();

            for (String name : names) {
                dates.add(new DateObject(name, db.getLogTime(name)));
            }

            DateObject[] exempt = new DateObject[threshold];

            for (int i = 0; i < threshold; i++) {
                // By default set the value to the first entry in the ArrayList
                exempt[i] = dates.get(0);

                // Check for any dates occurring before the date of index 0
                for (DateObject d : dates) {
                    if (d.getDate().before(exempt[i].getDate())) exempt[i] = d;
                }

                // Remove the latest entry from the Dates ArrayList so as to
                // prevent accidental duplication of entries
                dates.remove(exempt[i]);
            }

            boolean shouldKick = true;

            // See if the player who is logging in is allowed
            for (DateObject d : exempt) {
                if (d.getPlayer().equalsIgnoreCase(player)) shouldKick = false;
            }

            if (shouldKick) {
                // If IPC should ban when performing a Secure-Mode Kick
                if (config.getBoolean("should-ban-on-secure-kick") &&
                        config.getBoolean("should-manage-bans")) {
                    String msg = config.getString("ban-message");
                    Player p = e.getPlayer();
                    p.setBanned(true);

                    // Set Banned Flag for player in IPC Database
                    db.banPlayer(player, msg);
                    e.setKickMessage(msg);

                    // Kick Player
                    e.setResult(Result.KICK_BANNED);
                } else {
                    e.setKickMessage(config.getString("secure-kick-message"));

                    // Kick Player
                    e.setResult(Result.KICK_OTHER);
                }

                return false;
            }
        }

        return true;
    }

    private ArrayList<String> getUniqueAccounts(String player) {
        ArrayList<String> unique_names = new ArrayList<String>();
        UserObject user = db.getUserObject(player);

        if (user.getNumberOfIPs() == 0) return null;

        // Fetch IPObjects from UserObject
        ArrayList<IPObject> ipos = new ArrayList<IPObject>();

        for (String s : user.getIPs()) {
            ipos.add(db.getIPObject(s));
        }

        for (IPObject ipo : ipos) {
            if (ipo.getNumberOfUsers() == 1) {
                if (ipo.getUsers().contains(player.toLowerCase())) continue;
            }

            // Append each account, plus punctuation to the string builder
            for (String s : ipo.getUsers()) {
                if (!unique_names.contains(s.toLowerCase())) {
                    unique_names.add(s.toLowerCase());
                }
            }
        }

        return unique_names;
    }

}
