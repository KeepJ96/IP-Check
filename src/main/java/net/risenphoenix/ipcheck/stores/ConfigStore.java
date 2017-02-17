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
import net.risenphoenix.commons.configuration.ConfigurationOption;
import net.risenphoenix.commons.configuration.ConfigurationOption.ConfigOptionType;
import net.risenphoenix.commons.stores.ConfigurationStore;

public class ConfigStore extends ConfigurationStore {

    public ConfigStore(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public void initializeStore() {
        // Language Selector
        this.add(new ConfigurationOption(ConfigOptionType.String,
                "language"));

        // Database Selection and Options
        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "use-mysql"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "dbUsername"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "dbPassword"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "dbHostname"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "dbName"));

        this.add(new ConfigurationOption(ConfigOptionType.Integer,
                "dbPort"));

        // Database Reset Flag
        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "dbGenerated"));

        // Main Plugin Settings
        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "notify-on-login"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "warn-on-rejoin-attempt"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "descriptive-notice"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "secure-mode"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "active-mode"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "should-ban-on-secure-kick"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "should-manage-bans"));

        // Thresholds
        this.add(new ConfigurationOption(ConfigOptionType.Integer,
                "min-account-notify-threshold"));

        this.add(new ConfigurationOption(ConfigOptionType.Integer,
                "secure-kick-threshold"));

        // Plugin Messages
        this.add(new ConfigurationOption(ConfigOptionType.String,
                "secure-kick-message"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "ban-message"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "kick-message"));

        // Metrics / Updater Controls
        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "disable-update-detection"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "disable-metrics-monitoring"));

        // Geo-IP Services
        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "use-geoip-services"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "allow-geoip-download"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "use-country-blacklist"));

        this.add(new ConfigurationOption(ConfigOptionType.Boolean,
                "use-blacklist-as-whitelist"));

        this.add(new ConfigurationOption(ConfigOptionType.StringList,
                "country-blacklist"));

        this.add(new ConfigurationOption(ConfigOptionType.String,
                "blocked-message"));

        this.add(new ConfigurationOption(ConfigOptionType.Integer,
                "config-version"));
    }
}
