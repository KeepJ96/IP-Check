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

package net.risenphoenix.ipcheck.objects;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.configuration.ConfigurationManager;
import net.risenphoenix.commons.localization.LocalizationManager;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.util.FormatFilter;
import net.risenphoenix.ipcheck.util.ListFormatter;
import net.risenphoenix.ipcheck.util.TimeCalculator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ReportObject {

    private Plugin plugin;
    private DatabaseController db;
    private ConfigurationManager config;
    private LocalizationManager local;

    /* IP Cross-Checking Variables */
    private ArrayList<StringBuilder> SBs;
    private ArrayList<String> singleAlts;
    private ArrayList<String> uniqueAlts;

    /* UUID Cross-Checking Variables */
    private boolean useUUIDResults = false;
    private String UUIDResults;

    private OfflinePlayer player;
    private boolean forPlayer;

    public ReportObject(IPCheck ipCheck) {
        this.plugin = ipCheck;
        this.db = ipCheck.getDatabaseController();
        this.config = ipCheck.getConfigurationManager();
        this.local = ipCheck.getLocalizationManager();

        this.SBs = new ArrayList<StringBuilder>();
        this.singleAlts = new ArrayList<String>();
        this.uniqueAlts = new ArrayList<String>();
    }

    public void onExecute(CommandSender sender, String arg) {
        // IP Filter for differentiating player-names from IPs
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

        // Determine if the input is an IP or a Player name
        forPlayer = (!arg.toLowerCase().matches(ip_filter));

        // Fetch Offline Player for use with the Database
        if (forPlayer) {
            this.player = Bukkit.getOfflinePlayer(arg);

            // Ban State Updater to keep Ban Records up-to-date.
            if (player.isBanned()) {
                // To prevent re-banning and to preserve ban messages.
                if (!db.getUserObject(arg).getBannedStatus()) {
                    db.banPlayer(player.getName(),
                            config.getString("ban-message"));
                }
            } else {
                if (db.getUserObject(arg).getBannedStatus()) {
                    db.unbanPlayer(player.getName());
                }
            }

            FetchResult fResult = this.fetchPlayerData(arg);

            // If the Fetch Result returned a NO_FIND status, return.
            if (fResult == FetchResult.NOT_FOUND) {
                this.plugin.sendPlayerMessage(sender,
                        this.local.getLocalString("NO_FIND"));
                return;
            }
        } else {
            IPObject ipo = this.db.getIPObject(arg);
            this.singleAlts = ipo.getUsers();

            if (ipo.getNumberOfUsers() == 0) {
                this.plugin.sendPlayerMessage(sender,
                        this.local.getLocalString("NO_FIND"));
                return;
            }
        }

        // Output Report
        this.outputHead(sender, arg);
        this.outputBody(sender, arg);
        this.outputFoot(sender, arg);
    }

    // Report Header
    private void outputHead(CommandSender sender, String arg) {
        this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        if (!forPlayer) {
            if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                this.plugin.sendPlayerMessage(sender, ChatColor.GOLD +
                        this.local.getLocalString("REPORT_HEAD_ONE") + " " +
                        ChatColor.GREEN + arg + ChatColor.GOLD +
                        " ... " + ChatColor.RED +
                        this.singleAlts.size(), false);
            } else {
                this.plugin.sendPlayerMessage(sender, ChatColor.GOLD +
                        this.local.getLocalString("REPORT_HEAD_TWO") + " " +
                        ChatColor.RED + this.singleAlts.size(), false);
            }
        } else {
            this.plugin.sendPlayerMessage(sender, ChatColor.GOLD +
                    this.local.getLocalString("REPORT_HEAD_ONE") + " " +
                    ChatColor.GREEN + arg + ChatColor.GOLD + " ... " +
                    ChatColor.RED + this.uniqueAlts.size(), false);
        }
    }

    // Report Body
    private void outputBody(CommandSender sender, String arg) {
        if ((forPlayer && uniqueAlts.size() > 0) ||
                (!forPlayer && singleAlts.size() > 0)) {

            this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);

            if (!forPlayer) {
                String output = new ListFormatter(this.singleAlts)
                        .getFormattedList().toString();

                if (sender.hasPermission("ipcheck.showip")) {
                    this.plugin.sendPlayerMessage(sender,
                            ChatColor.LIGHT_PURPLE +
                                    this.local.getLocalString("REPORT_BODY_ONE"),
                            false);

                    this.plugin.sendPlayerMessage(sender, ChatColor.YELLOW +
                            output, false);
                } else {
                    this.plugin.sendPlayerMessage(sender,
                            ChatColor.LIGHT_PURPLE +
                                    this.local.getLocalString("REPORT_BODY_TWO")
                                    + " " + ChatColor.YELLOW + output, false);
                }

                return;
            } else {
                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_BODY_TWO") + " ",
                        false);

                // For each StringBuilder, fetch the IP address from the
                // beginning and store it in a separate variable, then remove
                // it from the list along with the divisor character '|'.
                for (int i = 0; i < this.SBs.size(); i++) {
                    StringBuilder sb = this.SBs.get(i);
                    StringBuilder ipAddress = new StringBuilder();
                    String full = sb.toString();

                    for (int j = 0; j < full.length(); j++) {
                        if (full.charAt(j) != '|') {
                            ipAddress.append(full.charAt(j));
                        } else {
                            break;
                        }
                    }

                    // Create an output omitting the IP-Address and divisor char
                    String out = full.replace(ipAddress.toString() + "|", "");

                    // If Sender can see IPs, show the IP, else show the place-
                    // holder text "####:"
                    if (!sender.hasPermission("ipcheck.showip") &&
                            !sender.isOp()) {
                        this.plugin.sendPlayerMessage(sender,
                                ChatColor.RED + "####:", false);
                    } else {
                        String ipAdd = ipAddress.toString();

                        this.plugin.sendPlayerMessage(sender, ChatColor.RED +
                                ipAdd + ":", false);
                    }

                    // Display the associated accounts
                    this.plugin.sendPlayerMessage(sender, out, false);

                    // Place a Spacer in-between the different IP listings
                    if (i < (SBs.size() - 1)) sender.sendMessage("");
                }
            }
        } else {
            this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
            this.plugin.sendPlayerMessage(sender,
                    this.local.getLocalString("REPORT_BODY_FOUR"), false);
        }

        // UUID Hook, placed outside normal IF logic so that it will show
        // regardless of Alternate Result amounts.
        if (forPlayer) {
            // UUID Results Hook
            if (this.useUUIDResults) {
                this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                            "------------------------------------------------",
                        false);
                this.plugin.sendPlayerMessage(sender,
                        ChatColor.LIGHT_PURPLE + this.local
                                .getLocalString("UUID_HEAD") + " ", false);
                this.plugin.sendPlayerMessage(sender, ChatColor.YELLOW +
                        this.UUIDResults + " ", false);
            }
        }
    }

    // Report Footer
    private void outputFoot(CommandSender sender, String arg) {
        this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        if (forPlayer) {
            if (this.player != null) {
                // Display Last Known IP
                String ipOutput = this.db.getLastKnownIP(arg);

                if (sender.hasPermission("ipcheck.showip") || sender.isOp()) {
                // Output Last Known IP
                    this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE
                            + this.local.getLocalString("REPORT_FOOT_LAST_IP") +
                            " " + ChatColor.YELLOW + ipOutput, false);
                }

                // Display Player Country
                String out = IPCheck.getInstance().getBlockManager()
                        .getCountry(ipOutput);

                String country = ChatColor.YELLOW + " " + ((out != null) ? out :
                        local.getLocalString("LOCATION_UNAVAILABLE"));

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_LOCATION") +
                        country, false);

                // Display Time since Last Login
                String lastLog = ChatColor.YELLOW + " " +
                        new TimeCalculator(arg).getLastTime();

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_PTIME") +
                        lastLog, false);

                this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                        "------------------------------------------------",
                        false);

                // Display Ban Status
                String banStatus = (this.db.isBannedPlayer(player.getName())) ?
                        ChatColor.RED + " True" : ChatColor.GREEN + " False";

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_PBAN") +
                        banStatus, false);

                // Display Exemption Status
                String exmStatus = (this.db.isExemptPlayer(player.getName())) ?
                        ChatColor.GREEN + " True" : ChatColor.RED + " False";

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_PEXM") +
                        exmStatus, false);

                // Display Protection Status
                String proStatus = (db.isProtectedPlayer(player.getName())) ?
                        ChatColor.GREEN + " True" : ChatColor.RED + " False";

                this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                        this.local.getLocalString("REPORT_FOOT_PPRO") +
                        proStatus, false);

                // Display Rejoin Exemption Status
                String exmRejoinStatus = (this.db.isRejoinExemptPlayer(arg)) ?
                         ChatColor.RED + " False" : ChatColor.GREEN + " True";

                if (db.isBannedPlayer(player.getName())) {
                    this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                            "------------------------------------------------",
                            false);
                    this.plugin.sendPlayerMessage(sender,
                            ChatColor.LIGHT_PURPLE + this.local
                            .getLocalString("REPORT_FOOT_PREXM") +
                            exmRejoinStatus, false);
                }
            } else {
                this.plugin.sendPlayerMessage(sender, ChatColor.RED +
                        "ERROR: " + ChatColor.GOLD +
                        this.local.getLocalString("REPORT_FOOT_ERR"), false);
            }
        } else {
            // Display IP Country
            String out = IPCheck.getInstance().getBlockManager()
                    .getCountry(arg);

            String country = ChatColor.YELLOW + " " + ((out != null) ? out :
                    local.getLocalString("LOCATION_UNAVAILABLE"));

            this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    this.local.getLocalString("REPORT_FOOT_LOCATION") +
                    country, false);

            // Display Ban Status
            String banStatus = (this.db.isBannedIP(arg)) ?
                    ChatColor.RED + " True" : ChatColor.GREEN + " False";

            this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    this.local.getLocalString("REPORT_FOOT_IBAN") +
                    banStatus, false);

            // Display Exemption Status
            String exmStatus = (this.db.isExemptIP(arg)) ?
                    ChatColor.GREEN + " True" : ChatColor.RED + " False";

            this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    this.local.getLocalString("REPORT_FOOT_IEXM") +
                    exmStatus, false);

            // Display Rejoin Exemption Status
            String rexmStatus = (!this.db.isRejoinExemptIP(arg)) ?
                    ChatColor.GREEN + " True" : ChatColor.RED + " False";

            this.plugin.sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    this.local.getLocalString("REPORT_FOOT_IREXM") +
                    rexmStatus, false);
        }

        // Display Ban Message if one Exists
        if (forPlayer) {
            if (sender.hasPermission("ipcheck.showbanreason") || sender.isOp()) {
                if (player != null) {
                    if (this.db.isBannedPlayer(arg)) {
                        String banMsg = this.db.getBanMessage(player.getName());

                        if (banMsg == null || banMsg.length() <= 0) {
                            banMsg = this.local
                                    .getLocalString("REPORT_BAN_GENERIC");
                        }

                        this.plugin.sendPlayerMessage(sender,
                                ChatColor.LIGHT_PURPLE +
                                        this.local.getLocalString("REPORT_BAN_HEAD") +
                                        " " + ChatColor.YELLOW + banMsg, false);
                    }
                }
            }
        }

        // End Report Output
        this.plugin.sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);
    }

    // Player Data Fetch
    private FetchResult fetchPlayerData(final String arg) {
        // Fetch UserObject from the Database
        UserObject user = this.db.getUserObject(arg);

        // If there were zero IPs returned, return with NOT_FOUND status.
        if (user.getNumberOfIPs() == 0) return FetchResult.NOT_FOUND;

        /*=================== IP CROSS-REFERENCING ===================*/

        // Fetch IPObjects from UserObject
        ArrayList<IPObject> ipos = new ArrayList<IPObject>();

        // Get IP-Addresses
        for (String s : user.getIPs()) ipos.add(this.db.getIPObject(s));

        // Parse Information Strings
        for (IPObject ipo : ipos) {

            /* If the current IPObject being parsed has one user linked and the
             * linked user is the same user that we're checking, skip it.*/
            if (ipo.getNumberOfUsers() == 1) {
                if (ipo.getUsers().contains(arg.toLowerCase())) continue;
            }

            // Create New String Builder
            StringBuilder sb = new StringBuilder();

            // Append the leading IP-Address, plus a splitter character '|'
            sb.append(ipo.getIP() + "|");

            // Create a FormatFilter for the ListFormatter
            FormatFilter fFilter = new FormatFilter() {
                private ArrayList<String> inputs = new ArrayList<>();

                @Override
                public String execute(String input) {
                    // Filter out duplicate entries
                    if (inputs.contains(input.toLowerCase())) {
                        return null;
                    } else {
                        inputs.add(input.toLowerCase());
                    }

                    // Filter out entries of the search term
                    return (!(input.equalsIgnoreCase(arg))) ? input : null;
                }
            };

            // Create a Formatted List with the Account Names and append it to
            // the current StringBuilder.
            ListFormatter format = new ListFormatter(ipo.getUsers(), fFilter);
            sb.append(format.getFormattedList());

            // Create Unique_Accounts List, ignoring case.
            for (String s : ipo.getUsers()) {
                String val = s.toLowerCase();
                if (!val.equalsIgnoreCase(arg)) {
                    if (!uniqueAlts.contains(val)) uniqueAlts.add(val);
                }
            }

            /* Add the StringBuilder to the holder ArrayList if there are
             * accounts linked to the IP that are not filtered out. */
            if (!sb.toString().equals(ipo.getIP() + "|")) SBs.add(sb);
        }

        /*=================== UUID CROSS-REFERENCING ===================*/

        // If Server is Online, allow Report to display UUID Results
        if (Bukkit.getOnlineMode()) {
            this.useUUIDResults = true;

            // If User UUID field is not null, check database for other
            // accounts sharing the same UUID.
            if (user.getUUID() != null) {
                ArrayList<String> uuid_links =
                        this.db.getPlayersByUUID(user.getUUID());

                ArrayList<String> unique_uuid_results =
                        new ArrayList<String>();

                for (String s : uuid_links) {
                    if (s.toLowerCase().equals(arg.toLowerCase())) continue;
                    if (!unique_uuid_results.contains(s))
                        unique_uuid_results.add(s);
                }

                if (unique_uuid_results.size() == 0) {
                    this.UUIDResults = this.local.getLocalString("NO_UUID_RES");
                } else {
                    ListFormatter format =
                            new ListFormatter(unique_uuid_results);

                    this.UUIDResults = format.getFormattedList().toString();
                }
            } else {
                this.UUIDResults = this.local.getLocalString("NO_UUID_RES");
            }
        }

        return FetchResult.GOOD;
    }

    private enum FetchResult {
        GOOD,
        NOT_FOUND
    }
}
