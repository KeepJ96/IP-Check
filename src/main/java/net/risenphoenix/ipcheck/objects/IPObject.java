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

import java.util.ArrayList;

public class IPObject {

    private String IP;
    private ArrayList<String> Users;
    private boolean isBanned;
    private boolean isExempt;
    private boolean isRejoinExempt;

    public IPObject(String IP, ArrayList<String> Users, boolean isBanned,
                    boolean isExempt, boolean isRejoinExempt) {
        this.IP = IP;
        this.Users = Users;
        this.isBanned = isBanned;
        this.isExempt = isExempt;
        this.isRejoinExempt = isRejoinExempt;
    }

    public IPObject(String IP, boolean isBanned) {
        this.IP = IP;
        this.isBanned = isBanned;
    }

    public final int getNumberOfUsers() {
        return this.Users.size();
    }

    public final String getIP() {
        return this.IP;
    }

    public final ArrayList<String> getUsers() {
        return this.Users;
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
}
