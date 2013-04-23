package net.risenphoenix.jnk.ipcheck.commands;

public class ParseCommand {

	/*====== Command ID List ======
		Default              = 0
		Ban                  = 1
		Unban                = 2
		ExemptIP             = 3
		ExemptPlayer         = 4
		Unexempt             = 5
		Toggle               = 6
		Exempt-List_all      = 7
		Exempt-List_IP       = 8
		Exempt-List_Player   = 9
		Reload               = 10
		About                = 11 
		
		See Javadoc for execute() in this class for explanation of negative return values.*/
	
	/*** Returns the Integer Identifier of the Command to be called.
	 * @return -1 if an invalid command was passed, -2 if invalid sub-command or no sub-command was specified, or -3 if no command was passed. **/
	public static int execute(String[] args) {
		if (args.length > 0) {
			// Ban Command
			if (args[0].equalsIgnoreCase("ban")) return 1;
			
			// Unban Command
			if (args[0].equalsIgnoreCase("unban")) return 2;
			
			// Exempt Command
			if (args[0].equalsIgnoreCase("exempt")) {
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("ip")) {
						return 3;
					} else if (args[1].equalsIgnoreCase("player")) {
						return 4;
					} else if (args[1].equalsIgnoreCase("remove")) {
						return 5;
					} else {
						return -2; // Invalid Sub-Command
					}
				} else {
					return -2; // No Sub-Command
				}
			}
			
			// Toggle Command
			if (args[0].equalsIgnoreCase("toggle")) return 6;
			
			// Exempt-List Command
			if (args[0].equalsIgnoreCase("exempt-list")) {
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("ip")) {
						return 8;
					} else if (args[1].equalsIgnoreCase("player")) {
						return 9;
					} else {
						return -2; // Invalid Sub-Command
					}
				} else {
					return 7;
				}
			}
			
			// Reload Command
			if (args[0].equalsIgnoreCase("reload")) return 10;
			
			// About Command
			if (args[0].equalsIgnoreCase("about")) return 11;
			
			if (args.length < 2) return 0; // If it was not one of the above commands and arguments is greater than 0, then pass it to the default check command.
		} else {
			return -3; // No Command was Given.
		}
		
		return -1; // Invalid Command
	}
	
}
