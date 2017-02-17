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

package net.risenphoenix.ipcheck.commands.block;

import com.maxmind.geoip.LookupService;
import net.risenphoenix.ipcheck.IPCheck;

import java.util.List;

public class BlockManager {

    private boolean isEnabled = false;

    private IPCheck ipc;
    private LookupService ls;

    public BlockManager(IPCheck ipc) {
        this.ipc = ipc;

        if (ipc.getConfigurationManager().getBoolean("use-geoip-services")) {
            this.isEnabled = true;
            this.ls = ipc.getGeoIPObject().getLookupService();
        }
    }

    public boolean getStatus() {
        return this.isEnabled;
    }

    public String getCountry(String ip) {
        if (!isEnabled) return null;
        if (ls != null) {
            return ls.getCountry(ip).getName();
        } else { return null; }
    }

    public String getCountryID(String ip) {
        if (!isEnabled) return null;
        if (ls != null) {
            return ls.getCountry(ip).getCode();
        } else { return null; }
    }

    // Method Accepts Two-Character Country ID
    public boolean isBlockedCountry(String country) {
        // Fetch Block List
        List<String> c_block = ipc.getConfigurationManager()
                .getStringList("country-blacklist");

        // Scan Block List for Match
        for (String s : c_block) {
            if (s.equalsIgnoreCase(country)) return true;
        }

        return false;
    }

    // Method Accepts Two-Character Country ID
    public boolean blockCountry(String country) {
        // Fetch Block List
        List<String> c_block = ipc.getConfigurationManager()
                .getStringList("country-blacklist");

        // Fetch Addition Result
        boolean result = !c_block.contains(country.toLowerCase());
        if (result) c_block.add(country.toLowerCase());

        // Store Configuration Value
        ipc.getConfigurationManager().setConfigurationOption(
                "country-blacklist", c_block);

        return result;
    }

    // Method Accepts Two-Character Country ID
    public boolean unblockCountry(String country) {
        // Fetch Block List
        List<String> c_block = ipc.getConfigurationManager()
                .getStringList("country-blacklist");

        // Fetch Removal Result
        boolean result = c_block.contains(country.toLowerCase());
        if (result) c_block.remove(country.toLowerCase());

        // Store Configuration Value
        ipc.getConfigurationManager().setConfigurationOption(
                "country-blacklist", c_block);

        return result;
    }

    public LookupService getLookupService() {
        return this.ls;
    }
}
