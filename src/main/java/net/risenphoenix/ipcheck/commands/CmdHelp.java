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

package net.risenphoenix.ipcheck.commands;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CmdHelp extends Command {

    public CmdHelp(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        setName(getLocalString("CMD_HELP"));
        setHelp(getLocalString("HELP_HELP"));
        setSyntax("ipc help [PAGE]");
        setPermissions(new Permission[]{new Permission("ipcheck.use")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Create a temporal storage area for the commands
        ArrayList<Command> list = new ArrayList<Command>();

        /* Fetch the global command list from the Command Manager. Filter
         * commands by canExecute(). */
        for (Command cmd:this.getPlugin().getCommandManager().getAllCommands()){
            if (cmd.canExecute(sender)) list.add(cmd);
        }

        // Sort List Alphabetically
        List<Command> cmdSort = new ArrayList<Command>(list);
        Collections.sort(cmdSort, new Comparator<Command>() {
            @Override
            public int compare(Command o1, Command o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // Return Sorted List to the Global List.
        list = new ArrayList<Command>(cmdSort);

        // Split List into "Pages", which can be viewed by argument.
        int pages = (list.size() / 4);
        if (list.size() % 4 != 0) pages++;

        // Set default Page Number
        int pageNumber = 1;

        // Output Help Display
        sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // Attempt Page Number Parse
        if (args.length == 2) {
            try {
                pageNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sendPlayerMessage(sender, getLocalString("ILL_ARGS_ERR"));
            }
        }

        // Confirm page-number is within bounds.
        if (pageNumber > pages) {
            pageNumber = pages;
        } else if (pageNumber < 1) {
            pageNumber = 1;
        }

        // Create Control Variable for Command Display
        int commandNumber = 0;

        // Output Command Information
        for (Command cmd:list) {
            if (commandNumber >= ((pageNumber - 1) * 4) &&
                    commandNumber < ((pageNumber - 1) * 4) + 4) {
                sendPlayerMessage(sender, ChatColor.GREEN + cmd.getName() + ":",
                        false);
                sendPlayerMessage(sender, ChatColor.YELLOW + " " +
                        cmd.getHelp(), false);
                sendPlayerMessage(sender, ChatColor.RED + "    " + "Syntax:" +
                        ChatColor.LIGHT_PURPLE + " /" + cmd.getSyntax(), false);

                // Blank Space between Commands
                if (commandNumber < (((pageNumber - 1) * 4) + 4) - 1) {
                    sendPlayerMessage(sender, "", false);
                }
            }

            // Step Control Variable
            commandNumber++;
        }

        // Display Information at Bottom of Page w/ Command
        if (pageNumber < pages) {
            sendPlayerMessage(sender, " ", false);
            sendPlayerMessage(sender, ChatColor.RED + "Type " +
                    ChatColor.YELLOW + "/ipc help " + (pageNumber + 1) +
                    ChatColor.RED + " for more information.", false);
        }

        sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                "------------------------------------------------", false);

        // To prevent possible memory leaks
        list.clear();
    }
}
