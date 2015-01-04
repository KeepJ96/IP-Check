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

package net.risenphoenix.ipcheck.commands.toggle;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.commons.configuration.ConfigurationManager;

public class ToggleOption {

    private Plugin plugin;
    private String optionID;
    private String displayID;
    private String[] callValues;

    /**
     * @param plugin Plugin Instance
     * @param optionID Option ID in Plugin Configuration
     * @param displayID Display ID stored in Localization Store
     * @param callValues Call ID references for Toggle Command
     */
    public ToggleOption(Plugin plugin, String optionID, String displayID,
                        String[] callValues) {
        this.plugin = plugin;
        this.optionID = optionID;
        this.displayID = displayID;
        this.callValues = callValues;
    }

    public final boolean onExecute() {
        /* Fetch Configuration Manager and current option value (inverted)
         * for use with change operation. */
        ConfigurationManager config = this.plugin.getConfigurationManager();
        boolean newValue = !config.getBoolean(this.optionID);

        // Set Configuration Option to new value
        config.setConfigurationOption(this.optionID, newValue);

        // Return new value
        return newValue;
    }

    public final String[] getCallValues() {
        return this.callValues;
    }

    public final String getOptionID() {
        return this.optionID;
    }

    public final String getDisplayID() {
        return plugin.getLocalizationManager().getLocalString(displayID);
    }

}
