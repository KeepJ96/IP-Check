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

package net.risenphoenix.ipcheck.stores;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.stores.LocalizationStore;

public class LocaleStore extends LocalizationStore {

    public LocaleStore(final Plugin plugin) {
        super(plugin);
    }
    
    @Override
    public void initializeStore() {
        this.add("NO_FIND", "The player or IP specified could not be found.");
        this.add("PLAYER_EXEMPT_SUC", "Player added to Exemption List!");
        this.add("IP_EXEMPT_SUC", "IP-address added to Exemption List!");
        this.add("EXEMPT_DEL_SUC", "Exemption successfully removed!");
        this.add("EXEMPT_DEL_ERR", "Exemption specified does not exist.");
        this.add("EXEMPTION_FAIL", "An error occurred while attempting to add " +
                "the exemption.");
        this.add("EXEMPT_LIST_IP", "Exempt IPs:");
        this.add("EXEMPT_LIST_PLAYER", "Exempt Players:");
        this.add("TOGGLE_SECURE", "Secure-Mode");
        this.add("TOGGLE_NOTIFY", "Notify-On-Login");
        this.add("TOGGLE_DETAIL", "Descriptive-Notify");
        this.add("TOGGLE_ACTIVE", "Active-Mode");
        this.add("TOGGLE_BLACKLIST", "Country-Black-List");
        this.add("TOGGLE_WHITELIST", "Country-White-List (invert)");
        this.add("TOGGLE_GEOIP", "GeoIP-Services");
        this.add("TOGGLE_REJOIN", "Rejoin-Warning");
        this.add("TOGGLE_INVALID", "You did not specify a valid option.");
        this.add("PURGE_SUC", "Successfully purged %s.");
        this.add("PURGE_ERR", "Failed to purge %s.");
        this.add("DISABLE_ERR", "This command has been disabled via configuration.");
        this.add("SCAN_CLEAN", "No players with multiple accounts are logged " +
                "in right now.");
        this.add("TIME_RANGE_EMPTY","No accounts were returned within the " +
                "date-range given.");
        this.add("RELOAD","Reload complete!");
        this.add("NO_MODIFY","No accounts were modified.");
        this.add("CMD_FETCH_ERR", "An error occurred while attempting to " +
                "fetch this Command from the Command Manager.");
        this.add("METRICS_ERR", "An error occurred while initializing the " +
                "Metrics system.");

        // Command Names
        this.add("CMD_CHECK","Check");
        this.add("CMD_BAN","Ban");
        this.add("CMD_UNBAN","Unban");
        this.add("CMD_EXEMPT","Exempt");
        this.add("CMD_UNEXEMPT","Unexempt");
        this.add("CMD_RELOAD","Reload");
        this.add("CMD_ABOUT","About");
        this.add("CMD_TOGGLE","Toggle");
        this.add("CMD_EXEMPT_LIST","Exempt-List (all)");
        this.add("CMD_EXEMPT_LIST_IP","Exempt-List (ip)");
        this.add("CMD_EXEMPT_LIST_PLAYER","Exempt-List (player)");
        this.add("CMD_HELP","Help");
        this.add("CMD_KICK","Kick");
        this.add("CMD_SBAN","Single-Ban");
        this.add("CMD_PURGE","Purge");
        this.add("CMD_SCAN","Scan");
        this.add("CMD_BANALL","Ban-All");
        this.add("CMD_UNBANALL","Unban-All");
        this.add("CMD_BLOCK", "Block");
        this.add("CMD_UNBLOCK", "Unblock");
        this.add("CMD_PROTECT", "Protect");
        this.add("CMD_UNPROTECT", "Unprotect");
        this.add("CMD_MODBAN", "Modify Ban");
        this.add("CMD_STATUS", "Status");

        // Command Help Documentation
        this.add("HELP_CHECK","Displays information about " +
                "the player or IP Specified.");
        this.add("HELP_BAN","Bans the player or IP " +
                "specified. In addition, this command will also ban any " +
                "alternative accounts associated, plus the IP-address.");
        this.add("HELP_UNBAN","Unbans the Player or IP " +
                "specified. Additionally, unbans any associated accounts, " +
                "plus the IP-address.");
        this.add("HELP_EXEMPT","Exempts the IP or player " +
                "specified from events-checking.");
        this.add("HELP_UNEXEMPT","Removes the specified " +
                "exemption from file.");
        this.add("HELP_RELOAD","Reloads the IP-Check plugin.");
        this.add("HELP_ABOUT","Displays Information about " +
                "IP-Check.");
        this.add("HELP_TOGGLE","Toggles the specified " +
                "option. For a list of options, type ''/ipc toggle help''");
        this.add("HELP_EXEMPT_LIST","Displays all players/" +
                "ips that are exempt from events-checking.");
        this.add("HELP_EXEMPT_LIST_IP","Displays all IPs " +
                "which are exempt from events-checking.");
        this.add("HELP_EXEMPT_LIST_PLAYER","Displays all " +
                "players who are exempt from events-checking.");
        this.add("HELP_HELP","Provides information about " +
                "all of the associated IP-Check Commands.");
        this.add("HELP_KICK","Kicks all players linked to " +
                "player or IP specified.");
        this.add("HELP_SBAN","Bans a single player from " +
                "your server.");
        this.add("HELP_PURGE","Deletes records of the IP or " +
                "Player name specified.");
        this.add("HELP_SCAN","Scans all players currently " +
                "online to check for any who may possess multiple accounts.");
        this.add("HELP_BANALL","Bans all accounts found " +
                "within specified time frame.");
        this.add("HELP_UNBANALL","Unbans all accounts found " +
                "within specified time frame.");
        this.add("HELP_BLOCK", "Blocks the country specified, preventing " +
                "anyone from joining the server from said country.");
        this.add("HELP_UNBLOCK", "Unblocks a blocked country, allowing " +
                "players from said country to join the server.");
        this.add("HELP_PROTECT", "Protects the specified player from being " +
                "banned by IP-Check.");
        this.add("HELP_UNPROTECT", "Allows the specified player to be banned " +
                "by IP-Check.");
        this.add("HELP_MODBAN", "Allows you to modify the ban message of " +
                "any banned player.");
        this.add("HELP_STATUS", "Displays IP-Check usage statistics.");

        // Other Messages
        this.add("SCAN_TITLE","Player Scan Results");
        this.add("SCAN_EXPLAIN","The following players were " +
                "found to have multiple accounts:");
        this.add("LOGIN_WARN","Warning!");
        this.add("LOGIN_EXPLAIN"," may have multiple " +
                "accounts!");
        this.add("REJOIN_WARN", "Notice!");
        this.add("REJOIN_EXPLAIN", " was kicked from the server due to a " +
                "previous IP-Ban.");
        this.add("TIME_STAMP_ERR","An error occurred while " +
                "attempting to parse a time stamp. This should never happen. " +
                "If you see this message, please contact the developers at " +
                "dev-bukkit and inform them of the circumstances that caused " +
                "this error.");
        this.add("SBAN_IP_HELP","To ban an IP address, use " +
                "'/ipc ban'");
        this.add("EXEMPT_LIST_TALLY","Total Exemptions:");
        this.add("TOGGLE_HEAD","List of Toggle Options:");
        this.add("ABOUT_TEXT","Version %s build %s by Jacob Keep (Jnk1296). " +
                "All rights reserved. Built against %s %s, build %s by %s. " +
                "This product includes GeoLite data created by MaxMind, " +
                "available from: http://www.maxmind.com/");

        // Report Messages
        this.add("REPORT_HEAD_ONE","Alternate Accounts found "+
                "for:");
        this.add("REPORT_HEAD_TWO","Alternate Accounts " +
                "found:");

        this.add("REPORT_BODY_ONE","The following players " +
                "connect with the above IP address:");
        this.add("REPORT_BODY_TWO","The following players " +
                "connect using the same IP address:");
        this.add("REPORT_BODY_THREE","Players and IPs " +
                "associated with the search term:");
        this.add("REPORT_BODY_FOUR","No alternate accounts " +
                "were found for this user.");

        this.add("REPORT_FOOT_LAST_IP","Last Known IP:");
        this.add("REPORT_FOOT_LOCATION","Last Location:");
        this.add("LOCATION_UNAVAILABLE","GeoIP Services unavailable.");

        this.add("REPORT_FOOT_PTIME", "Last Login:");

        this.add("REPORT_FOOT_PBAN","Player Banned:");
        this.add("REPORT_FOOT_PEXM","Player Exempt:");

        this.add("REPORT_FOOT_PPRO", "Player Protected:");

        this.add("REPORT_FOOT_PREXM", "Will warn on Rejoin Attempt:");

        this.add("REPORT_FOOT_ERROR","Player object returned "+
                "was NULL");

        this.add("REPORT_FOOT_IBAN","IP Banned:");
        this.add("REPORT_FOOT_IEXM","IP Exempt:");
        this.add("REPORT_FOOT_IREXM","Warn when banned players rejoin under this IP:");

        this.add("REPORT_BAN_HEAD", "Ban Reason:");
        this.add("REPORT_BAN_GENERIC", "No Message");

        this.add("UUID_HEAD", "UUID Matches:");
        this.add("NO_UUID_RES", "There were no results for players " +
                "sharing this UUID.");

        // Exempt / Unexempt Messages
        this.add("EXEMPT_PROMPT", "Please select the Exemption you wish to " +
                "create:");
        this.add("UNEXEMPT_PROMPT", "Please select the Exemption you wish to " +
                "remove:");
        this.add("EXEMPT_PROMPT_CANCEL", "Prompt aborted.");

        // GeoIP / Block Messages
        this.add("GEOIP_DB_MISSING", "The GeoIP.dat database could not be found. " +
                "This file must be available in order for GeoIP Services to " +
                "function properly. It can be downloaded at: " +
                "http://geolite.maxmind.com/download/geoip/database/" +
                "GeoLiteCountry/GeoIP.dat.gz");
        this.add("GEOIP_DB_READ_ERR", "An error occurred while attempting to " +
                "read the GeoIP database.");
        this.add("GEOIP_DOWNLOAD", "Attempting automatic download of GeoIP " +
                "database from: http://geolite.maxmind.com/download/geoip/" +
                "database/GeoLiteCountry/GeoIP.dat.gz...");
        this.add("GEOIP_DISABLED", "GeoIP Services have been disabled via configuration.");
        this.add("BLOCK_CMD_DISABLE", "The Block Commands have been disabled " +
                "because the Block Manager failed to initialize.");
        this.add("BLOCK_SUC", "Country ID successfully added to black list!");
        this.add("BLOCK_ERR", "This country ID has already been black-listed.");
        this.add("BLOCK_HELP", "Please visit " +
                "http://dev.bukkit.org/bukkit-plugins/ip-check-jnk/pages/" +
                "country-ids/ for a list of country IDs.");
        this.add("UNBLOCK_SUC", "Country ID successfully removed from black-list!");
        this.add("UNBLOCK_ERR", "This Country ID was not found in the black-list.");
        this.add("BLACK_LIST_OFF", "You must enable the Country Black-List " +
                "for this configuration to take effect.");

        this.add("PROTECT_SUC", "Player successfully protected!");
        this.add("PROTECT_IP_ERR", "You cannot protect or unprotect an IP address.");
        this.add("UNPROTECT_SUC", "Player successfully unprotected!");

        this.add("MODBAN_IP", "IP addresses cannot have ban messages.");
        this.add("MODBAN_SUC", "Successfully modified ban message.");

        // Stats Messages
        this.add("STATS_HEADER", "Plugin Usage Statistics:");
        this.add("STATS_PVER", "IP-Check Version: ");
        this.add("STATS_LVER", "RP-Commons Version: ");
        this.add("STATS_DB_TYPE", "Database Type: ");
        this.add("STATS_JVER", "Java Version: ");
        this.add("STATS_OS", "Operating System: ");
        this.add("STATS_OS_ARCH", "System Architecture: ");
        this.add("STATS_PLOG", "Total Players Logged: ");
        this.add("STATS_ILOG", "Total IPs Logged: ");
        this.add("STATS_PEXM", "Total Players Exempt: ");
        this.add("STATS_IEXM", "Total IPs Exempt: ");
        this.add("STATS_RPEXM", "Total Players Rejoin Exempt: ");
        this.add("STATS_RIEXM", "Total IPs Rejoin Exempt: ");
        this.add("STATS_PBAN", "Total Players Banned: ");
        this.add("STATS_IBAN", "Total IPs Banned: ");
        this.add("STATS_PLOGS", "Player Logins this Session: ");
        this.add("STATS_PBANS", "Players Banned this Session: ");
        this.add("STATS_PUNBANS", "Players Unbanned this Session: ");
        this.add("STATS_WARNS", "Login Warnings this Session: ");
        this.add("STATS_KICKS", "Kicks Issued this Session: ");
        this.add("STATS_SECURE", "Secure Mode Status: ");
        this.add("STATS_ACTIVE", "Active Mode Status: ");
        this.add("STATS_BLACKLIST", "Country Black-List Status: ");

        this.add("VER_COMP_ERR", "This version of IP-Check is not fully " +
                "compatible with the version of Bukkit you are running. " +
                "Please upgrade your Bukkit installation or downgrade " +
                "IP-Check to v2.0.2.");

        this.add("DEV_BUILD_WARN", "NOTICE: This is a development build of " +
                "IP-Check! Automatic Updater and Metrics have been disabled! " +
                "If you are seeing this message, please alert the plugin " +
                "developer, as this should not appear.");

        this.add("CONFIG_VER_MISMATCH", "WARNING: Your configuration " +
                "is out of date! Please make note of your configuration " +
                "settings, then delete your config.yml " +
                "and restart the plugin.");
    }

}
