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

package net.risenphoenix.ipcheck.commands.exempt;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.objects.IPObject;
import net.risenphoenix.ipcheck.objects.UserObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.permissions.Permission;

public class CmdUnexempt extends Command {

    private DatabaseController db;
    private String argument;

    public CmdUnexempt(final Plugin plugin, String[] callArgs,
                       CommandType type) {
        super(plugin, callArgs, type);
        this.db = IPCheck.getInstance().getDatabaseController();

        setName(getLocalString("CMD_UNEXEMPT"));
        setHelp(getLocalString("HELP_UNEXEMPT"));
        setSyntax("ipc unexempt <PLAYER | IP>");
        setPermissions(new Permission[]{
                new Permission("ipcheck.use"),
                new Permission("ipcheck.unexempt")});

        setConversationFactory(new ConversationFactory(plugin));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // Store Regex Filter to differentiate between user and IP
        String ip_filter = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        argument = args[1];

        // If the argument is an IP address, validate it and fetch IPO.
        if (argument.matches(ip_filter)) {
            if (db.isValidIP(argument)) {
                // Fetch IPO from Database
                IPObject ipo = db.getIPObject(argument);

                // If the IPO is exempt, unexempt it, else throw an error.
                if (ipo.getExemptStatus()) {
                    db.unexemptIP(argument);
                } else {
                    sendPlayerMessage(sender, getLocalString("NO_MODIFY"));
                    return;
                }

                sendPlayerMessage(sender, getLocalString("EXEMPT_DEL_SUC"));
            } else {
                sendPlayerMessage(sender, getLocalString("NO_FIND"));
            }

        /* If the argument is a player name, fetch the UPO and verify whether
         * it is exempt or not. If it is, unexempt it, otherwise throw a
         * NO_MODIFY error.*/
        } else {
            if (db.isValidPlayer(argument)) {
                // Prompt for the Exemption Type
                getConversationFactory().withFirstPrompt(this);
                getConversationFactory()
                        .buildConversation((Conversable) sender).begin();
            } else {
                sendPlayerMessage(sender, getLocalString("NO_FIND"));
            }
        }
    }

    @Override
    public String getPromptText(ConversationContext context) {
        context.getForWhom().sendRawMessage(getPlugin()
                .formatPlayerMessage(getLocalString("UNEXEMPT_PROMPT")));

        context.getForWhom().sendRawMessage(ChatColor.GOLD + "  (" +
                ChatColor.RED + "0" + ChatColor.GOLD + "). " +
                ChatColor.YELLOW + "Cancel");

        context.getForWhom().sendRawMessage(ChatColor.GOLD + "  (" +
                ChatColor.RED + "1" + ChatColor.GOLD + "). " +
                ChatColor.YELLOW + "Login-Check Exemption");

        return ChatColor.GOLD + "  (" + ChatColor.RED + "2" + ChatColor.GOLD +
                "). " + ChatColor.YELLOW + "Rejoin-Warn Exemption";
    }

    @Override
    public Prompt acceptValidatedInput(ConversationContext con, String s) {
        if (s.equalsIgnoreCase("one") || s.equals("1")) {
            removeLoginExemption(con);
        } else if (s.equalsIgnoreCase("two") || s.equals("2")) {
            removeRejoinExemption(con);
        } else if (s.equalsIgnoreCase("zero") || s.equalsIgnoreCase("cancel") ||
                s.equals("0")) {
            con.getForWhom().sendRawMessage(getPlugin().formatPlayerMessage(
                getLocalString("EXEMPT_PROMPT_CANCEL")));
        }

        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public boolean isInputValid(ConversationContext context, String s) {
        return  (s.equalsIgnoreCase("one") || s.equals("1")
              || s.equalsIgnoreCase("two") || s.equals("2")
              || s.equalsIgnoreCase("zero") || s.equals("0"));
    }

    private void removeLoginExemption(ConversationContext context) {
        UserObject upo = db.getUserObject(argument);

        // Exempt UPO or throw error
        if (upo.getExemptStatus()) {
            db.unexemptPlayer(argument);
        } else {
            context.getForWhom().sendRawMessage(getPlugin()
                    .formatPlayerMessage(getLocalString("NO_MODIFY")));
            return;
        }

        context.getForWhom().sendRawMessage(getPlugin()
                .formatPlayerMessage(getLocalString("EXEMPT_DEL_SUC")));
    }

    private void removeRejoinExemption(ConversationContext context) {
        UserObject upo = db.getUserObject(argument);

        // Exempt UPO or throw error
        if (upo.getRejoinExemptStatus()) {
            db.setRejoinExemptPlayer(argument, false);
        } else {
            context.getForWhom().sendRawMessage(getPlugin()
                    .formatPlayerMessage(getLocalString("NO_MODIFY")));
            return;
        }

        context.getForWhom().sendRawMessage(getPlugin()
                .formatPlayerMessage(getLocalString("EXEMPT_DEL_SUC")));
    }
}