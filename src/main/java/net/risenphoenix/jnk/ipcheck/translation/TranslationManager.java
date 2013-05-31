package net.risenphoenix.jnk.ipcheck.translation;

/**
 *
 * @author FR34KYN01535
 */
public class TranslationManager {
    public static final String PLUG_NAME = "[IP-Check] ";
    public static final String INIT_BACKEND = "Initializing Flat-File Backend Manager...";
    public static final String DEINIT_BACKEND = "Shutting down Backend Manager...";
    public static final String BAN_LIST_READ_ERR = "Error occurred while attempting to read banned-ips.txt!";
    public static final String FLAT_FILE_WRITE_ERR = "An error occurred while attempting to write to a database document.";
    public static final String FLAT_FILE_READ_ERR = "An error occurred while attempting to read a database document.";
    public static final String FLAT_FILE_GEN_ERR = "An error occurred while attempting to generate a new database document!";

    public static String confWriteErr = "Failed to generate Configuration File!";
    public static String confReadErr = "Failed to read Configuration File!";
    public static String exmpWriteErr = "Failed to write to Exemption File!";
    public static String exmpReadErr = "Failed to read Exemption File!";
    public static String exmpGenErr = "Failed to generate Exemption File!";
    public static String banGenErr = "Failed to generate Banned File!";
    public static String banWriteErr = "Failed to write to Banned File!";
    public static String banReadErr = "Failed to read Banned File!";

    public static String COE1 = "Failed to parse configuration option: ";
    public static String COE2 = ". Is the configuration file formatted correctly?";

    public static final String NO_FIND = "The player or IP specified could not be found.";
    public static final String PLAYER_FILE_READ_ERR = "Error occurred while attempting to read player file!";
    public static final String NO_PERM_ERR = "You don't have permission to do that!";
    public static final String NUM_ARGS_ERR = "Incorrect Number of Arguments.";
    public static final String ILL_ARGS_ERR = "Illegal Argument(s) were passed into the command.";
    public static final String NO_RECENT = "You have not searched a player yet.";
    public static final String PLAYER_EXEMPT_SUC = "Player added to exemption list!";
    public static final String IP_EXEMPT_SUC = "IP-Address added to exemption list!";
    public static final String EXEMPTION_FAIL = "Sorry. :( Something went wrong. The exemption could not be added.";
    public static final String TOGGLE_SECURE = "Secure-Mode set to: ";
    public static final String TOGGLE_NOTIFY = "Notify-On-Login set to: ";
    public static final String TOGGLE_DETAIL = "Descriptive-Notify set to: ";
    public static final String TOGGLE_ERR = "An error occurred while attempting to set state of toggle.";
    public static final String EXEMPTION_DEL_SUC = "Exemption successfully removed!";
    public static final String EXEMPTION_DEL_ERR = "Exemption specified does not exist.";
    public static final String ERROR_LOG_RMDR = "An error occurred! A log summary of this error has been saved to IP-Check's directory under ''Error_Reports''";

    public static final String PURGE_SUC = "Sucessfully purged %s.";
    public static final String PURGE_ERR = "Failed to purge %s!";
    
}
