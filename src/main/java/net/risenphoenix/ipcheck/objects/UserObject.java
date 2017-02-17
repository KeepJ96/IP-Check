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

package net.risenphoenix.ipcheck.objects;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class UserObject {

    private String User;
    private UUID uuid;
    private ArrayList<String> IPs;
    private boolean isBanned;
    private boolean isExempt;
    private boolean isRejoinExempt;
    private boolean isProtected;

    public UserObject(String User, UUID uuid, ArrayList<String> IPs,
                      boolean isBanned, boolean isExempt,
                      boolean isRejoinExempt, boolean isProtected) {
        this.User = User;
        this.uuid = uuid;
        this.IPs = IPs;
        this.isBanned = isBanned;
        this.isExempt = isExempt;
        this.isRejoinExempt = isRejoinExempt;
        this.isProtected = isProtected;
    }

    public UserObject(String user, boolean isBanned) {
        this.User = user;
        this.isBanned = isBanned;
    }

    public final String getUser() {
        return this.User;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public final ArrayList<String> getIPs() {
        return this.IPs;
    }

    public final int getNumberOfIPs() {
        return this.IPs.size();
    }

    public final boolean getBannedStatus() {
        return this.isBanned;
    }

    public final boolean getExemptStatus() {
        return this.isExempt;
    }

    public boolean getRejoinExemptStatus() {
        return isRejoinExempt;
    }

    public boolean getProtectedStatus() {
        return isProtected;
    }
}
