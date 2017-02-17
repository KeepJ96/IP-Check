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

package net.risenphoenix.ipcheck;

import net.risenphoenix.commons.Plugin;
import net.risenphoenix.ipcheck.commands.block.BlockManager;
import net.risenphoenix.ipcheck.database.DatabaseController;
import net.risenphoenix.ipcheck.events.PlayerLoginListener;
import net.risenphoenix.ipcheck.objects.GeoIPObject;
import net.risenphoenix.ipcheck.objects.StatsObject;
import net.risenphoenix.ipcheck.stores.CmdStore;
import net.risenphoenix.ipcheck.stores.ConfigStore;
import net.risenphoenix.ipcheck.stores.LocaleStore;
import net.risenphoenix.ipcheck.util.DateStamp;
import net.risenphoenix.ipcheck.util.Messages;
import net.risenphoenix.ipcheck.util.Metrics;
import net.risenphoenix.ipcheck.util.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

public class IPCheck extends Plugin implements Listener {

    // Instance and Main System Objects
    private static IPCheck instance;
    private DatabaseController dbController;
    private ConfigStore config;

    // Updater and Metrics Objects
    private Updater updater;
    private Metrics metrics;

    // Statistics Object
    private StatsObject statsObject;

    // GeoIP Service Objects
    private GeoIPObject geoIPOBject = null;
    private BlockManager blockManager = null;

    // Control used mainly in the event of an in-plugin Reload.
    private boolean hasRegistered = false;

    // Used for Development Purposes Only (hard disable for automatic updater)
    private boolean isDevBuild = true;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        new PlayerLoginListener(this, e);
    }

    @Override
    public void onStartup() {
        instance = this;

        // Set Basic Plugin Variables
        this.setPluginName(ChatColor.GOLD, "IP-Check");
        this.setMessageColor(ChatColor.YELLOW);

        // Register the Player Login Listener
        if (!hasRegistered) {
            getServer().getPluginManager().registerEvents(this, this);
            this.hasRegistered = true; // Prevents Re-registration with Bukkit
        }

        // Initialize Configuration
        this.config = new ConfigStore(this);
        this.getConfigurationManager().initializeConfigurationStore(config);

        // Initialize Default Localization
        LocaleStore locStore = new LocaleStore(this);
        this.getLocalizationManager().appendLocalizationStore(locStore);

        // Initialize GeoIP Services
        if (this.getConfigurationManager().getBoolean("use-geoip-services")) {
            this.geoIPOBject = new GeoIPObject(this);
        }

        this.blockManager = new BlockManager(this);

        // Initialize Database Controller
        if (this.getConfigurationManager().getBoolean("use-mysql")) {
            // MySQL Database Initialization
            dbController = new DatabaseController(this,
                    this.getConfigurationManager().getString("dbHostname"),
                    this.getConfigurationManager().getInteger("dbPort"),
                    this.getConfigurationManager().getString("dbName"),
                    this.getConfigurationManager().getString("dbUsername"),
                    this.getConfigurationManager().getString("dbPassword")
            );
        } else {
            // SQLite Database Initialization
            dbController = new DatabaseController(this);
        }

        // Initialize Commands
        CmdStore cmdStore = new CmdStore(this);
        this.getCommandManager().registerStore(cmdStore);

        // Initialize Statistics
        this.statsObject = new StatsObject(this);

        // Development Build Hook
        if (!this.isDevBuild) {
            // Auto-Update Checker
            if (!getConfigurationManager()
                    .getBoolean("disable-update-detection")) {
                updater = new Updater(this, 55121, this.getFile(),
                        Updater.UpdateType.DEFAULT, false);
            }

            // Metrics Monitoring
            if (!getConfigurationManager()
                    .getBoolean("disable-metrics-monitoring")) {
                try {
                    metrics = new Metrics(this);
                    metrics.start();
                } catch (IOException e) {
                    sendConsoleMessage(Level.SEVERE, getLocalizationManager()
                            .getLocalString("METRICS_ERR"));
                }
            }
        } else {
            sendConsoleMessage(Level.INFO, getLocalizationManager()
                    .getLocalString("DEV_BUILD_WARN"));
        }

        // Display Random Message
        showRandomMessage();
    }

    @Override
    public void onShutdown() {
        dbController.getDatabaseConnection().closeConnection();
    }

    public static IPCheck getInstance() {
        return instance;
    }

    public DatabaseController getDatabaseController() {
        return this.dbController;
    }

    public StatsObject getStatisticsObject() {
        return this.statsObject;
    }

    public GeoIPObject getGeoIPObject() {
        return this.geoIPOBject;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public String getVersion() {
        return "2.0.6";
    }

    public int getBuildNumber() {
        return 2068;
    }

    private void showRandomMessage() {
        DateStamp ds = new DateStamp();
        String ran = Messages.getSeasonalMessage(ds.getCustomStamp("MM-dd"));

        if (ran != null) {
            this.sendConsoleMessage(Level.INFO, ran);
        } else {
            this.sendConsoleMessage(Level.INFO, Messages.getRandomMessage());
        }
    }

    public final Player[] getOnlinePlayers() {
        try {
            Collection<? extends Player> result = Bukkit.getOnlinePlayers();
            return result.toArray(new Player[result.size()]);
        } catch (NoSuchMethodError err) {
            sendConsoleMessage(Level.INFO, getLocalizationManager()
                    .getLocalString("VER_COMP_ERR"));
        }

        return new Player[0];
    }

}
