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

package net.risenphoenix.ipcheck.util;

import net.risenphoenix.ipcheck.IPCheck;
import net.risenphoenix.ipcheck.database.DatabaseController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCalculator {

    private String arg;
    private DatabaseController dbc;

    public TimeCalculator(String player) {
        this.arg = player;
        this.dbc = IPCheck.getInstance().getDatabaseController();
    }

    public String getLastTime() {
        // Define Date-Stamp Format
        SimpleDateFormat sParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Define Dates
        Date currentTime;
        Date lastTime;

        // Parse Current Time
        try {
            currentTime = sParse.parse(dbc.getCurrentTimeStamp());
            lastTime = sParse.parse(dbc.getLastTime(arg));
        } catch (ParseException e) {
            // Report Exception to Console
            e.printStackTrace();
            return "ERROR";
        }

        // Get Time difference (divide by 1000 so our calculations will work)
        long timeOffset = (currentTime.getTime() - lastTime.getTime()) / 1000;

        // Declare Time Storing Variables
        int days, hours, minutes, seconds;

        // Fetch Days
        days = (int) timeOffset / 86400; // Number of seconds in a day.
        timeOffset -= (days * 86400); // Reduce Time

        hours = (int) timeOffset / 3600; // Number of seconds in an hour.
        timeOffset -= (hours * 3600); // Reduce Time

        minutes = (int) timeOffset / 60; // Number of seconds in a minute.
        timeOffset -= (minutes * 60); // Reduce Time

        seconds = (int) timeOffset; // Remainder will be number of seconds.

        // Bump Seconds up by one in the event of a 0 value in order to allow
        // statement to remain grammatically correct.
        if (seconds == 0) seconds++;

        // Create Words
        String d = (days == 1) ? " Day, " : " Days, ";
        String h = (hours == 1) ? " Hour, " : " Hours, ";
        String m = (minutes == 1) ? " Minute, " : " Minutes, ";
        String s = (seconds == 1) ? " Second ago." : " Seconds ago.";

        // Create Output String
        return ((days > 0) ? days + d : "") +
               ((hours > 0) ? hours + h : "") +
               ((minutes > 0) ? minutes + m : "") +
               ((seconds > 0) ? seconds + s : "");
    }

}
