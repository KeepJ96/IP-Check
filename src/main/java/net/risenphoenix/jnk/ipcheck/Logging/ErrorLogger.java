package net.risenphoenix.jnk.ipcheck.Logging;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.Functions;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import org.bukkit.Bukkit;

public class ErrorLogger {

    private static File dir = new File("plugins/IP-check/Error_Reports");

    public void execute(Exception e) {
        DateStamp date = new DateStamp();
        String dateStamp = date.getErrorStamp();
        File path = new File(dir + "/" + dateStamp + ".txt");
        FileWriter f = null;

        Logger logger = Bukkit.getLogger();

        createFolder();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        String message = (
            Functions.getErrorMessage() + "\r\n" +
            "-------------------------------------\r\n" +
            IPcheck.VER_STRING + "\r\n" +
            "Compilation Date: " + IPcheck.COMP_DATE + "\r\n" +
            "-------------------------------------\r\n" +
            "Backend Manager Registrar: " + getRegistrar() + "\r\n" +
            "Current Operating Directory: " + System.getProperty("user.dir") + "\r\n" +
            "-------------------------------------\r\n" +
            "We're sorry, but an error occurred while attempting to perform \r\n" +
            "an internal operation. " +
            "The system reported the following error: \r\n" + "\r\n" + sw.toString() + "\r\n" +
            "If this is the first time you're received this error, you may \r\nattempt " +
            "to disregard it and continue normal use of the plugin.\r\nIf you continue " +
            "to receive this message, please submit this\r\nerror report to the developer at:\r\n" +
            "\r\nDev Bukkit: \r\nhttp://dev.bukkit.org/server-mods/ip-check-jnk/ \r\n" +
            "\r\nOr contact the developer at:\r\njnk1296@risenphoenix.net\r\n" +
            "-------------------------------------\r\n" + dateStamp
            );

        try {
            f = new FileWriter(path, true);
            f.write(message);
        } catch (Exception ee) {
            logger.severe(IPcheck.PLUG_NAME + ee.getMessage());
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (Exception eee) {
                    logger.severe(IPcheck.PLUG_NAME + eee.getMessage());
                }
            }
        }
    }

    private void createFolder() {
        if (!dir.exists()) dir.mkdir();
    }

    private String getRegistrar() {
        if (IPcheck.backend != null) {
            return IPcheck.backend.getRegistrar();
        } else {
            return "NOT_INITIALIZED";
        }
    }
}
