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

package net.risenphoenix.ipcheck.commands.ban;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.actions.ActionBanAll;
import net.risenphoenix.ipcheck.actions.ActionBroadcast;
import net.risenphoenix.ipcheck.util.MessageParser;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CmdBanAll extends Command {

    private IPCheck ipc;

    public CmdBanAll(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        this.ipc = IPCheck.getInstance();

        setName(this.getLocalString("CMD_BANALL"));
        setHelp(this.getLocalString("HELP_BANALL"));
        setSyntax("ipc banall <START_TIME> <STOP_TIME | now> [MESSAGE]");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.ban"),
                new Permission("ipcheck.banall")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        executeBan(sender, args, true);
    }

    // Command-Specific Methods and Classes

    /* The executable code for Ban-All has been placed into this container
     * method so that it can be accessed by Unban-all via getCommand() from the
     * Command Manager. */
    public void executeBan(CommandSender sender, String[] args, boolean ban) {
        // Does the Configuration Allow this command to execute?
        if (!this.getPlugin().getConfigurationManager()
                .getBoolean("should-manage-bans")) {
            this.sendPlayerMessage(sender, this.getLocalString("DISABLE_ERR"));
            return;
        }

        // Parse Ban Message if one exists
        String message = new MessageParser(args, 3).parseMessage();

        // Confirm Ban Message is not Empty
        if (message == null || message.length() <= 0) message =
                IPCheck.getInstance().getConfigurationManager()
                        .getString("ban-message");

        // Fetch database timestamp and parse it as a Date object
        SimpleDateFormat sParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime;

        // Parse the timestamp returned from the Database
        try {
            currentTime = sParse.parse(ipc.getDatabaseController()
                    .getCurrentTimeStamp());
        } catch (ParseException e) {
            // If an error occurs while parsing, abort the command
            this.sendPlayerMessage(sender, getLocalString("TIME_STAMP_ERR"));
            return;
        }

        /* Create two ArrayLists to hold ModifyItems, one for each of the two
         * timestamps. setOne is the set for the older of the two time-stamps,
         * while setTwo is the set for the newer of the time-stamps, and is
         * optional in it's usage. */
        ArrayList<ModifyItem> setOne = new ArrayList<ModifyItem>();
        ArrayList<ModifyItem> setTwo = null;
        boolean hasFirst = false; // Parse Control Flag

        // Fetch ModifyItems for each set if Arguments permit
        for (int i = 1; i < args.length; i++) {
            if (!hasFirst) {
                hasFirst = true;
                setOne = getModificationItems(args[i]);
            } else {
                if (!args[i].equalsIgnoreCase("now")) {
                    setTwo = getModificationItems(args[i]);
                }
            }
        }

        // Convert Dates to SQL Time-stamps
        Timestamp tsOne = new Timestamp(modifyDateStamp(
                new Date(currentTime.getTime()), setOne).getTime());
        Timestamp tsTwo = (setTwo != null) ? new Timestamp(modifyDateStamp(
                new Date(currentTime.getTime()), setTwo).getTime()) : null;

        // Convert Timestamps to Strings
        String timeArgOne = sParse.format(tsOne);
        String timeArgTwo = (tsTwo != null) ? sParse.format(tsTwo) :
                sParse.format(new Timestamp(currentTime.getTime()));

        // Execute Ban and retrieve affected account tally
        Object[] results = new ActionBanAll(IPCheck.getInstance(), timeArgOne,
                timeArgTwo, message, ban).execute();

        // Get affected account tally from results
        int count = (Integer) results[0];

        // Stats Link
        if (ban) {
            IPCheck.getInstance().getStatisticsObject().logPlayerBan(count);
        } else {
            IPCheck.getInstance().getStatisticsObject().logPlayerUnban(count);
        }

        // Set up Broadcast Notification
        ActionBroadcast ab;
        String broadcastMsg;

        if (ban) {
            if (count == 1) {
                broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.RED +
                        "%s" + ChatColor.GOLD + " was banned by " +
                        ChatColor.GREEN + "%s" + ChatColor.GOLD + " for: %s";
            } else {
                broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.GREEN +
                        "%s" + ChatColor.GOLD + " banned " + ChatColor.RED +
                        "%s" + ChatColor.GOLD + " accounts for: " + "%s";
            }
        } else {
            if (count == 1) {
                broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.RED +
                        "%s" + ChatColor.GOLD + " was banned by " +
                        ChatColor.GREEN + "%s.";
            } else {
                broadcastMsg = ChatColor.GOLD + "Player " + ChatColor.GREEN +
                        "%s" + ChatColor.GOLD + " unbanned " + ChatColor.RED +
                        "%s" + ChatColor.GOLD + " accounts.";
            }
        }

        // Determine which message should be shown by the broadcaster, if any.
        if (count > 0) {
            // Ban Messages
            if (ban) {
                if (count == 1) {
                    ab = new ActionBroadcast(broadcastMsg, new String[]{
                            results[1].toString(), sender.getName(), message},
                            new Permission[]{new Permission("ipcheck.seeban")},
                            false);

                    // Execute Broadcast
                    ab.execute();
                } else {
                    ab = new ActionBroadcast(broadcastMsg, new String[]{
                            sender.getName(), count + "", message},
                            new Permission[]{new Permission("ipcheck.seeban")},
                            false);

                    // Execute Broadcast
                    ab.execute();
                }
            // Unban Messages
            } else {
                if (count == 1) {
                    ab = new ActionBroadcast(broadcastMsg, new String[]{
                            results[1].toString(), sender.getName()},
                            new Permission[]{new Permission("ipcheck.seeban")},
                            false);

                    // Execute Broadcast
                    ab.execute();
                } else {
                    ab = new ActionBroadcast(broadcastMsg, new String[]{
                            sender.getName(), count + ""},
                            new Permission[]{new Permission("ipcheck.seeban")},
                            false);

                    // Execute Broadcast
                    ab.execute();
                }
            }
        } else {
            // If no accounts were modified, notify player of this.
            this.sendPlayerMessage(sender, this.getLocalString("NO_MODIFY"));
        }
    }

    // ModifyItem Creator
    private ArrayList<ModifyItem> getModificationItems(String arg) {
        // Create Storage for finished ModifyItems
        ArrayList<ModifyItem> modItems = new ArrayList<ModifyItem>();

        // Split Time Arguments into separate indices for proper parsing
        String[] values = arg.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        int length = values.length;

        /* Confirm there are an even amount of indices to parse, else drop the
         * odd index from the length size. */
        if (values.length % 2 != 0) length--;

        // Control Flag denoting if arguments are formatted as 1d2h or d1h2
        boolean startsNum = false;

        // Determine proper setting of Control Flag
        try {
            Integer.parseInt(values[0]);
            startsNum = true;
        } catch (NumberFormatException e) { /*ignore*/ }

        // Create ModifyItems
        for (int i = 0; i < (length - 1); i += 2) {
            // If the Control Flag is true, then arguments are formatted as 1d2h
            if (startsNum) {
                modItems.add(new ModifyItem(values[i + 1],
                        Integer.parseInt(values[i])));
            } else {
                modItems.add(new ModifyItem(values[i],
                        Integer.parseInt(values[i + 1])));
            }
        }

        // Return finished ModifyItems
        return modItems;
    }

    // ModifyItem Parser
    private Date modifyDateStamp(Date date, ArrayList<ModifyItem> modItems) {
        // Fetch date stamp from Date (divide by 1000 for easier modification)
        long stamp = date.getTime() / 1000;

        // Loop through ModifyItems and edit date stamp
        for (ModifyItem m : modItems) {
            // Days
            if (m.getModifier().equalsIgnoreCase("d")) {
                int val = m.getValue() * 86400; //Number of seconds in a day
                stamp -= val;
                continue;
            }

            // Hours
            if (m.getModifier().equalsIgnoreCase("h")) {
                int val = m.getValue() * 3600; // Number of seconds in an hour
                stamp -= val;
                continue;
            }

            // Minutes
            if (m.getModifier().equalsIgnoreCase("m")) {
                int val = m.getValue() * 60; // Number of seconds in a minute
                stamp -= val;
                continue;
            }

            // Seconds
            if (m.getModifier().equalsIgnoreCase("s")) {
                stamp -= m.getValue(); // Value is already in number of seconds
                continue;
            }
        }

        date = new Date(stamp * 1000); // Multiply by 1000 to fix original div.
        return date;
    }

    // ModifyItem used with argument parsing
    private class ModifyItem {
        private String modifier;
        private int value;

        ModifyItem(String modifier, int value) {
            this.modifier = modifier;
            this.value = value;
        }

        public String getModifier() {
            return this.modifier;
        }

        public int getValue() {
            return this.value;
        }
    }
}
