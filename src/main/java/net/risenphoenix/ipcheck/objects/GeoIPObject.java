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

import com.maxmind.geoip.LookupService;
import net.risenphoenix.commons.configuration.ConfigurationManager;
import net.risenphoenix.commons.localization.LocalizationManager;
import net.risenphoenix.ipcheck.IPCheck;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;

public class GeoIPObject {

    private LookupService ls = null;

    private IPCheck ipc;
    private LocalizationManager LM;
    private ConfigurationManager CM;

    public GeoIPObject(final IPCheck ipc) {
        this.ipc = ipc;
        this.LM = ipc.getLocalizationManager();
        this.CM = ipc.getConfigurationManager();

        initializeDatabase();
    }

    private void initializeDatabase() {
        File database;

        // If the user has not opt-out of the GeoIP services, initialize the
        // GeoIP Database.
        if (CM.getBoolean("use-geoip-services")) {
            database = new File(ipc.getDataFolder(), "GeoIP.dat");
        } else {
            return;
        }

        // If the database is not found, instruct the user on how to acquire it.
        if (!database.exists()) {
            // Attempt to download database
            downloadDatabase();

            if (!database.exists()) {
                ipc.sendConsoleMessage(Level.SEVERE,
                        LM.getLocalString("GEOIP_DB_MISSING"));
                return;
            }
        }

        // Attempt to initialize the Lookup Services
        try {
            ls = new LookupService(database);
        } catch (IOException e) {
            ipc.sendConsoleMessage(Level.SEVERE,
                    LM.getLocalString("GEOIP_DB_READ_ERR"));
        }
    }

    private void downloadDatabase() {
        String URL = "http://geolite.maxmind.com/download/geoip/database/" +
                "GeoLiteCountry/GeoIP.dat.gz";

        if (CM.getBoolean("allow-geoip-download")) {
            ipc.sendConsoleMessage(Level.INFO,
                    LM.getLocalString("GEOIP_DOWNLOAD"));

            try {
                URL dURL = new URL(URL);
                URLConnection conn = dURL.openConnection();

                conn.setConnectTimeout(8000);
                conn.connect();

                InputStream input = new GZIPInputStream(conn.getInputStream());

                File databaseLoc = new File(ipc.getDataFolder() + "/GeoIP.dat");

                OutputStream output = new FileOutputStream(databaseLoc);

                byte[] dlBuffer = new byte[2048];
                int length = input.read(dlBuffer);
                while (length >= 0) {
                    output.write(dlBuffer, 0, length);
                    length = input.read(dlBuffer);
                }

                output.close();
                input.close();
            } catch (IOException e) {
                ipc.sendConsoleMessage(Level.SEVERE, e.getMessage());
            }
        }
    }

    public LookupService getLookupService() {
        return this.ls;
    }

}
