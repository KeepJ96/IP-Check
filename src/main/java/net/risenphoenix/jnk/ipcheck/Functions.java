package net.risenphoenix.jnk.ipcheck;

import java.util.ArrayList;
import java.util.Random;

public class Functions {

	// Start-up/Misc. use Random Messages.
	private static final ArrayList<String> MESSAGES_RANDOM_1 = new ArrayList<String>() {
		
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
			add("This message brought to you by our proud sponsers at Aperture!");
			add("I wish I wish I was a fish!");
			add("Do you like green eggs and tofu turkey? :3");
			add("You do realize this is going on your tab, right?");
			add("You may be the victim of ingenuity.");
			add("There's a fix for that. ;)");
			add("Enjoys long walks on the beach and chit-chat!");
			add("Second only to Google Nose!");
			add("And in other news...");
			add("Let them eat wood! Wait what?..");
			add("You woke me at this hour??");
			add("Trolololol...");
			add("OPOSSUM!");
			add("Now bringing you 500 channels of high-quality static. :D");
			add("Insert witty comment here!");
                        add("Bring us the one they call Nub!");
	}};
	
	// Error Report Messages
	private static final ArrayList<String> MESSAGES_ERROR = new ArrayList<String>() {
		
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
			add("To keep you calm in the face of almost certain rage, smooth jazz will be deployed: In three... two... one.");
			add("It wasn't me!");
			add("You broke it, didn't you?");
			add("Now who do you suppose is going to have to clean up this mess? Hmm?");
			add("I'm sowwy. :3");
			add("Don't be upset. I still love you. <3");
			add("It was the creepers!");
			add("HACKZ!!!");
			add("Would a complementary breath ment cheer you up? :(");
			add("I'm 95% certain this has something to do with an error.");
			add("FEGELEIN! FEGELEIN! FEGELEIN!");
			add("That's all, folks!");
			add("Don't look at me. I don't know anything. I'm just an error. I simple, uselss, little error.");
			add("Now how did that happen?");
			add("Did you try pressing ctrl+alt+delete?");
			add("I'm fairly certain we can fix this. Maybe...");
			add("If this error continues to occur, you can contact us at our Indian-based Call Center at 555-555-LOLZ");
			add("And she is out of here!");
			add("Sadface. :(");
	}};
	
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
		if (date.equals("05-05")) return Language.PLUG_NAME + "So who here likes Mayonase?";
		if (date.equals("04-01")) return Language.PLUG_NAME + "Your current subscription expires today! Renew immediately! The current subscription price is: $19.95";
		if (date.equals("04-02")) return Language.PLUG_NAME + "April fools! Of course this plugin is free! :)";
		if (date.equals("04-12")) return Language.PLUG_NAME + "Happy Birthday Jnk!";
		if (date.equals("12-25")) return Language.PLUG_NAME + "Merry Christmas from IP-Check. =D";
		if (date.equals("02-14")) return Language.PLUG_NAME + "Won't you be my Valentine? <3";
		
		return null;
	}
}
