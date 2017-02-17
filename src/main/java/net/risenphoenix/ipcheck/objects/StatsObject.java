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

package net.risenphoenix.ipcheck.objects;

import net.risenphoenix.commons.database.DatabaseManager;
import net.risenphoenix.ipcheck.IPCheck;

import java.util.Map;

public class StatsObject {

    private IPCheck ipc;

    // Stat Storage
    private int bannedPlayerSession;
    private int logPlayerSession;
    private int warningIssuedSession;
    private int kickIssuedSession;
    private int unbannedPlayerSession;

    public StatsObject(final IPCheck ipc) {
        this.ipc = ipc;
    }

    public String getPluginVersion() {
        return ipc.getVersion();
    }

    public Map<String, String> getLibraryVersion() {
        return ipc.getVersionInfo();
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public String getOperatingSystemArch() {
        return System.getProperty("os.arch");
    }

    public int getPlayersLogged() {
        return ipc.getDatabaseController().fetchAllPlayers().size();
    }

    public int getIPsLogged() {
        return ipc.getDatabaseController().fetchAllIPs().size();
    }

    public int getPlayersExempt() {
        return ipc.getDatabaseController().getPlayerExemptList().size();
    }

    public int getIPsExempt() {
        return ipc.getDatabaseController().getIPExemptList().size();
    }

    public int getPlayersRejoinExempt() {
        return ipc.getDatabaseController().fetchRejoinExemptPlayers().size();
    }

    public int getIPsRejoinExempt() {
        return ipc.getDatabaseController().fetchRejoinExemptIPs().size();
    }

    public int getPlayersBanned() {
        return ipc.getDatabaseController().fetchBannedPlayers().size();
    }

    public int getIPsBanned() {
        return ipc.getDatabaseController().fetchBannedIPs().size();
    }

    public DatabaseManager.DatabaseType getDatabaseType() {
        return ipc.getDatabaseController().getDatabaseType();
    }

    public void logPlayerBan(int count) {
        this.bannedPlayerSession += count;
    }

    public void logPlayerJoin(int count) {
        this.logPlayerSession += count;
    }

    public void logWarningIssue(int count) {
        this.warningIssuedSession += count;
    }

    public void logKickIssue(int count) {
        this.kickIssuedSession += count;
    }

    public void logPlayerUnban(int count) {
        this.unbannedPlayerSession += count;
    }

    public int getBannedPlayerSession() {
        return bannedPlayerSession;
    }

    public int getLogPlayerSession() {
        return logPlayerSession;
    }

    public int getWarningIssuedSession() {
        return warningIssuedSession;
    }

    public int getKickIssuedSession() {
        return kickIssuedSession;
    }

    public int getUnbannedPlayerSession() {
        return unbannedPlayerSession;
    }

    public boolean getSecureStatus() {
        return ipc.getConfigurationManager().getBoolean("secure-mode");
    }

    public boolean getActiveStatus() {
        return ipc.getConfigurationManager().getBoolean("active-mode");
    }

    public boolean getBlackListStatus() {
        return ipc.getConfigurationManager().getBoolean("use-country-blacklist");
    }
}
