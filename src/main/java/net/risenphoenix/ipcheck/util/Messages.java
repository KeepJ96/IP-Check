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

import java.util.ArrayList;
import java.util.Random;

public class Messages {

    // Start-up/Misc. use Random Messages.
    private static final ArrayList<String> MESSAGES_RANDOM_1 =
            new ArrayList<String>() {

        private static final long serialVersionUID = 3159470443160594003L;

        {
            add("Now in cool blue!");
            add("I like apples!");
            add("It's a trap!");
            add("Account logger, server protector, hotdog vendor!");
            add("Keep the change.");
            add("Do you have a consumer rewards card?");
            add("Now with 25% less fat!");
            add("Have you ever been to WonderWorks?");
            add("Serving you since 1892.");
            add("I blame Sesame Street. :(");
            add("This day belongs to squids!");
            add("This message brought to you by our proud sponsors at " +
                    "Aperture!");
            add("I wish I wish I was a fish!");
            add("Do you like green eggs and tofu turkey? :3");
            add("You do realize this is going on your tab, right?");
            add("You may be the victim of ingenuity.");
            add("There's a fix for that. ;)");
            add("Enjoys long walks on the beach and chit-chat!");
            add("Second only to Google Nose!");
            add("And in other news...");
            add("Let them eat wood! Wait, what?..");
            add("You woke me at this hour?!");
            add("Trolololol...");
            add("OPOSSUM!");
            add("Now bringing you 500 channels of high-quality static. :D");
            add("Insert witty comment here!");
            add("Bring us the one they call Nub!");
            add("It's over 9,000!");
            add("We're off to meet the fish-man!");
            add("Good job, Boehner!");
            add("Will never threaten to stab you!");
            add("Freak out!");
            add("Here we snow again!");
            add("It's a glitch!");
            add("Oranges!");
            add("But did you know...");
            add("Would you like a Swiss Cake Roll?");
            add("Fact or Fiction?");
        }
    };

    // Error Report Messages
    private static final ArrayList<String> MESSAGES_ERROR =
            new ArrayList<String>() {

        private static final long serialVersionUID = -3593223368287276483L;

        {
            add("Oh noes!");
            add("But whyyyy? :(");
            add("Oh goodness me... it went everywhere.");
            add("Oopsie daisy.");
            add("Rats!");
            add("Darn it!");
            add("Whoops.");
            add("I tried really hard! Honest! :[");
            add("Please don't be mad.");
            add("I WAS FROZEN TODAY!");
            add("To keep you calm in the face of almost certain rage, smooth " +
                    "jazz will be deployed: In three... two... one.");
            add("It wasn't me!");
            add("You broke it, didn't you?");
            add("Now who do you suppose is going to have to clean up this " +
                    "mess? Hmm?");
            add("I'm sowwy. :3");
            add("Don't be upset. I still love you. <3");
            add("It was the creepers!");
            add("HACKZ!!!");
            add("Would a complementary breath mint cheer you up? :(");
            add("I'm 95% certain this has something to do with an error.");
            add("FEGELEIN! FEGELEIN! FEGELEIN!");
            add("That's all, folks!");
            add("Don't look at me. I don't know anything. I'm just an error. " +
                    "I simple, useless, little error.");
            add("Now how did that happen?");
            add("Did you try pressing ctrl+alt+del?");
            add("I'm almost certain we can fix this. Maybe...");
            add("If this error continues to occur, you can contact us at our " +
                    "Indian-based Call Center at 555-555-LOLZ");
            add("And she is out of here!");
            add("Sadface. :(");
            add("I'll... I'll just go sit in the corner and cry now.");
            add("I'll talk to you when you learn to behave yourself.");
            add("Perhaps this is life trying to tell you something?");
            add("And just when you thought you hit the bottom...");
            add("I'm absolutely sure this is how you fix it... Er, is it " +
                    "supposed to smoke like that?");
        }
    };

    // Get Random Message
    public static String getRandomMessage() {
        int random = new Random().nextInt(MESSAGES_RANDOM_1.size());

        return MESSAGES_RANDOM_1.get(random);
    }

    // Get Random Error-message
    public static String getErrorMessage() {
        int random = new Random().nextInt(MESSAGES_ERROR.size());

        return MESSAGES_ERROR.get(random);
    }

    public static String getSeasonalMessage(String date) {
        if (date.equals("01-01")) return "Happy New Year!";
        if (date.equals("02-14")) return "Won't you be my Valentine? <3";
        if (date.equals("03-09")) return "In remembrance...";
        if (date.equals("04-01")) return "Your current subscription expires " +
                "today! Renew immediately! The current subscription price " +
                "is: $49.95";
        if (date.equals("04-02")) return "April fools! Of course this plugin " +
                "is free! :)";
        if (date.equals("04-12")) return "Happy Birthday Jnk!";
        if (date.equals("05-05")) return "So who here likes Mayonase?";
        if (date.equals("07-04")) return "Happy Fourth of July!";
        if (date.equals("12-25")) return "Merry Christmas from IP-Check. =D";

        return null;
    }

}
