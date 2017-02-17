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

package net.risenphoenix.ipcheck.commands.toggle;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.commands.Command;
import net.risenphoenix.commons.commands.CommandType;
import net.risenphoenix.commons.configuration.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public class CmdToggle extends Command {

    private ConfigurationManager config;
    private ArrayList<ToggleOption> options;

    public CmdToggle(final Plugin plugin, String[] callArgs, CommandType type) {
        super(plugin, callArgs, type);

        this.config = getPlugin().getConfigurationManager();
        this.options = new ArrayList<>();

        /* Options Initialization */

        options.add( // Login Notifications
            new ToggleOption(getPlugin(), "notify-on-login", "TOGGLE_NOTIFY",
                new String[]{"login-notify", "notification", "notify"})
        );

        options.add( // Descriptive Notifications
            new ToggleOption(getPlugin(), "descriptive-notice", "TOGGLE_DETAIL",
                new String[]{"detail-notify", "detail", "dn"})
        );

        options.add( // Secure Mode
            new ToggleOption(getPlugin(), "secure-mode", "TOGGLE_SECURE",
                new String[]{"secure-mode", "secure", "sm"})
        );

        options.add( // Active Mode
            new ToggleOption(getPlugin(), "active-mode", "TOGGLE_ACTIVE",
                new String[]{"active-mode", "active", "am"})
        );

        options.add( // Country Black-List
            new ToggleOption(getPlugin(), "use-country-blacklist",
                "TOGGLE_BLACKLIST", new String[]{"country-block", "black-list",
                    "cb"})
        );

        options.add( // Use Country Blacklist as Whitelist
            new ToggleOption(getPlugin(), "whitelist-mode", "TOGGLE_WHITELIST",
                new String[]{"cb-whitelist", "whitelist", "wl"})
        );

        options.add( // GeoIP Services
            new ToggleOption(getPlugin(), "use-geoip-services", "TOGGLE_GEOIP",
                new String[]{"geoip-services", "geoip", "gs"})
        );

        options.add( // Rejoin Attempt
            new ToggleOption(getPlugin(), "warn-on-rejoin-attempt",
                "TOGGLE_REJOIN", new String[]{"rejoin-attempt", "rejoin", "ra"})
        );

        // Command Initialization
        setName(getLocalString("CMD_TOGGLE"));
        setHelp(getLocalString("HELP_TOGGLE"));
        setSyntax("ipc toggle <OPTION_ID | help>");
        setPermissions(new Permission[]{new Permission("ipcheck.use"),
                new Permission("ipcheck.toggle")});
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        ToggleOption option = null;

        // Obtain Applicable ToggleOption
        for (ToggleOption to : this.options) {
            String[] callValues = to.getCallValues();

            for (int i = 0; i < callValues.length; i++) {
                if (args[1].equalsIgnoreCase(callValues[i])) {
                    option = to;
                    break;
                }
            }

            if (option != null) break;
        }

        // Help Argument Output
        if (args[1].equalsIgnoreCase("help")) {
            sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "---------------------------------------------", false);

            for (ToggleOption to : this.options) {
                StringBuilder sb = new StringBuilder();
                String[] callValues = to.getCallValues();

                sb.append(" " + to.getDisplayID() + ":" + ChatColor.YELLOW +
                        " < | ");

                for (int i = 0; i < callValues.length; i++) {
                    sb.append(ChatColor.LIGHT_PURPLE + callValues[i] +
                            ChatColor.YELLOW + " | ");
                }

                sb.append(ChatColor.YELLOW + ">");
                sendPlayerMessage(sender, sb.toString(), false);
            }

            sendPlayerMessage(sender, ChatColor.DARK_GRAY +
                    "---------------------------------------------", false);
            return;
        }

        // If the ToggleOption is null, return
        if (option == null) {
            sendPlayerMessage(sender, getLocalString("TOGGLE_INVALID"));
            return;
        }

        // If the ToggleOption is not null, execute toggle and fetch return
        Boolean newValue = option.onExecute();
        ChatColor color = (newValue) ? ChatColor.GREEN : ChatColor.RED;

        // Output Result
        sendPlayerMessage(sender, option.getDisplayID() + " set to: " + color +
                newValue);
    }
}
