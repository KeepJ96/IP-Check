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

import java.util.ArrayList;

public class ActionBanAll {

    private DatabaseController db;
    private ConfigurationManager config;

    private String tsOne;
    private String tsTwo;
    private String message;
    private boolean banning;

    public ActionBanAll(final IPCheck ipcheck, String timeStampOne,
                        String timeStampTwo, String message, boolean banning) {
        IPCheck ipc = ipcheck;
        this.db = ipc.getDatabaseController();
        this.config = ipc.getConfigurationManager();

        this.tsOne = timeStampOne;
        this.tsTwo = timeStampTwo;
        this.message = message;
        this.banning = banning;
    }

    public Object[] execute() {
        // Fetch ArrayList of User Objects created between the two timestamps
        ArrayList<UserObject> pResults = db.getPlayersByDate(tsOne, tsTwo);
        int validAccounts = 0; // Number of banned/unbanned accounts

        int depthUser = 0; // Used for generating SQL to prevent limit breach
        int depthIP = 0; // Used for generating SQL to prevent limit breach

        // Used for Broadcast when only one account is banned
        String acctName = "";

        // Form SQL Strings
        if (pResults.size() > 0) { // If there were accounts returned
            // StringBuilder storage for each of the SQL Commands
            ArrayList<StringBuilder> sbBanUser = new ArrayList<StringBuilder>();
            ArrayList<StringBuilder> sbBanIP = new ArrayList<StringBuilder>();

            // Temporal Storage for SQL Command Building
            StringBuilder userBanString = new StringBuilder();
            StringBuilder ipBanString = new StringBuilder();

            // Confirm given message is not empty
            String banMsg = (message == null || message.length() <= 0) ?
                    config.getString("ban-message") : message;

            // Create the SQL Strings
            for (UserObject upo : pResults) {
                // Fetch Last Known IP of the UPO
                String ip = db.getLastKnownIP(upo.getUser());

                // If the UPOs Last IP is not equal to the banning flag
                if (db.isBannedPlayer(ip) != banning) {
                    // Append IP to SQL command with additional parameters
                    ipBanString.append(ip + "' or ip='");
                    depthIP++;

                    /* Check if SQL Command Size is too large. If it is, move
                     * it from temporal storage to the ArrayList. SQL Commands
                     * have a limit of ~65000 characters, so setting the limit
                     * to 50000 will provide plenty of leeway. In addition,
                     * SQL sets an "Expression Tree Depth Limit" of 1000,
                     * meaning there can't be more than 1000 different options
                     * per argument of code. That said, the commands also need
                     * to be delimited if and when they hit the depth limit. */
                    if (ipBanString.length() >= 50000 || depthIP >= 998) {
                        sbBanIP.add(ipBanString);
                        ipBanString = new StringBuilder();
                        depthIP = 0;
                    }
                }

                /* Is the UPO already banned? If so, skip it and move on,
                otherwise append the UPO to the SQL Command String.*/
                if (upo.getBannedStatus() != banning) {
                    /* Store the first banned account in a separate variable
                     * so that if it is the only account that is banned, the
                     * name can be used in an ActionBroadcast message. */
                    if (validAccounts == 0) acctName = upo.getUser();

                    // Update Affected Accounts tally
                    validAccounts++;

                    /* If we are banning and the current player is online, kick
                     * them with the message specified or the default config
                     * message abd set their banned status to true on Bukkit.*/
                    if (banning) {
                        Player p = Bukkit.getPlayer(upo.getUser());

                        if (p != null) {
                            if (!db.isProtectedPlayer(p.getName())) {
                                p.kickPlayer(banMsg);
                                p.setBanned(true);
                            }
                        }

                        // If we are not banning, unban the player on Bukkit.
                    } else {
                        OfflinePlayer p =
                                Bukkit.getOfflinePlayer(upo.getUser());

                        if (p != null) {
                            p.setBanned(false);
                        }
                    }

                    // Append username to SQL command with additional parameters
                    if ((!banning) || (banning && !db.isProtectedPlayer(
                            upo.getUser()))) {
                        userBanString.append(upo.getUser().toLowerCase() +
                                "' or lower(username)='");
                        depthUser++;
                    }

                    /* Check if SQL Command Size is too large. If it is, move
                     * it from temporal storage to the ArrayList. SQL Commands
                     * have a limit of ~65000 characters, so setting the limit
                     * to 50000 will provide plenty of leeway. In addition,
                     * SQL sets an "Expression Tree Depth Limit" of 1000,
                     * meaning there can't be more than 1000 different options
                     * per argument of code. That said, the commands also need
                     * to be delimited if and when they hit the depth limit. */
                    if (userBanString.length() >= 50000 || depthUser >= 998) {
                        sbBanUser.add(userBanString);
                        userBanString = new StringBuilder();
                        depthUser = 0;
                    }
                }
            }

            // Add remaining Commands left in Temporal Storage to ArrayLists.
            sbBanIP.add(ipBanString);
            sbBanUser.add(userBanString);

            // Execute User Ban
            if (sbBanUser.get(0).length() > 0) {
                for (StringBuilder sb : sbBanUser) {
                    /* Remove the last 21 characters from the command (where
                     * additional parameters were added in anticipation of the
                     * next value being appended. */
                    String syntax = sb.toString().substring(0,
                            sb.toString().length() - 21);
                    // Execute ban with Database
                    db.batchBanPlayers(syntax, banMsg, banning);
                }
            }

            // Execute IP Ban
            if (sbBanIP.get(0).length() > 0) {
                for (StringBuilder sb : sbBanIP) {
                    /* Remove the last 8 characters from the command (where
                     * additional parameters were added in anticipation of the
                     * next value being appended. */
                    String syntax = sb.toString().substring(0, sb.length() - 8);
                    // Execute ban with Database
                    db.batchBanIPs(syntax, banning);
                }
            }
        }

        // Return Values
        return new Object[]{validAccounts, acctName};
    }
}
