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

package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.objects.StatsObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdStatus extends Command {

    public CmdStatus(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_STATUS"));
        setHelp(getLocalString("HELP_STATUS"));
        setSyntax("ipc status [adv | advanced | etc]");
        setPermissions(new Permission[]{new Permission("ipcheck.use")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Stats Object
        StatsObject stats = IPCheck.getInstance().getStatisticsObject();

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        sendPlayerMessage(sender, getLocalString("STATS_HEADER"));

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // IPC Ver.
        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_PVER") + ChatColor.YELLOW +
                stats.getPluginVersion(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_LVER") + ChatColor.YELLOW +
                stats.getLibraryVersion().get("VERSION"), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_DB_TYPE") + ChatColor.YELLOW +
                stats.getDatabaseType(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_JVER") + ChatColor.YELLOW +
                stats.getJavaVersion(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_OS") + ChatColor.YELLOW +
                stats.getOperatingSystem(), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_OS_ARCH") + ChatColor.YELLOW +
                stats.getOperatingSystemArch(), false);

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        if (args.length == 2) {
            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_PLOG") + ChatColor.YELLOW +
                    stats.getPlayersLogged(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_ILOG") + ChatColor.YELLOW +
                    stats.getIPsLogged(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_PEXM") + ChatColor.YELLOW +
                    stats.getPlayersExempt(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_IEXM") + ChatColor.YELLOW +
                    stats.getIPsExempt(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_RPEXM") + ChatColor.YELLOW +
                    stats.getPlayersRejoinExempt(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_RIEXM") + ChatColor.YELLOW +
                    stats.getIPsRejoinExempt(), false);

            // Border
            getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_PBAN") + ChatColor.YELLOW +
                    stats.getPlayersBanned(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_IBAN") + ChatColor.YELLOW +
                    stats.getIPsBanned(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_PLOGS") + ChatColor.YELLOW +
                    stats.getLogPlayerSession(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_PBANS") + ChatColor.YELLOW +
                    stats.getBannedPlayerSession(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_PUNBANS") + ChatColor.YELLOW +
                    stats.getUnbannedPlayerSession(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_WARNS") + ChatColor.YELLOW +
                    stats.getWarningIssuedSession(), false);

            sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                    getLocalString("STATS_KICKS") + ChatColor.YELLOW +
                    stats.getKickIssuedSession(), false);

            // Border
            getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "------------------------------------------------", false);
        }

        String isTrue = ChatColor.GREEN + "True",
               isFalse = ChatColor.RED + "False";

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_SECURE") + ChatColor.YELLOW +
                ((stats.getSecureStatus()) ? isTrue : isFalse), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_ACTIVE") + ChatColor.YELLOW +
                ((stats.getActiveStatus()) ? isTrue : isFalse), false);

        sendPlayerMessage(sender, ChatColor.LIGHT_PURPLE +
                getLocalString("STATS_BLACKLIST") + ChatColor.YELLOW +
                ((stats.getBlackListStatus()) ? isTrue : isFalse), false);

        // Border
        getPlugin().sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);
    }
}
