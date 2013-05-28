package net.risenphoenix.jnk.ipcheck;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;
import org.bukkit.Bukkit;

public class Essentials{
	
    // Message
    private static final String PLAYER_FILE_READ_ERR = "Error occurred while attempting to read essentials player file!";

    // Folder Path
    private static File playersDir = new File("plugins/Essentials/userdata");

    // Logger
    private static final Logger logger = Bukkit.getLogger();

    // As of IP-Check 3.0, the Essentials Backend System has been depreciated. This is the only remaining method which we need.
    public ArrayList<String> loadFile() {
        ArrayList<String> list = new ArrayList<String>();

        File path = null;
        File[] playerFiles = playersDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File path, String name) {
                if (name.toLowerCase().endsWith(".yml") && !name.toLowerCase().startsWith("nation_") && !name.toLowerCase().startsWith("town_")) {
                    return true; 
                } else {
                    return false;
                }
            }
        });

        for (int i = 0; i < playerFiles.length; i++) {
            path = new File("" + playerFiles[i]);
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String player = playerFiles[i].toString();
            player = player.replace(playersDir.toString(), "");
            player = player.replace("" + player.charAt(0), "");
            sb.append(player.replace(".yml", ""));
            sb.append("|");

            try {
                FileInputStream fstream = new FileInputStream(path);
                DataInputStream in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                while ((strLine = br.readLine()) != null) {
                    if (strLine.startsWith("ipAddress: ")) {
                        strLine = strLine.replace("ipAddress: ", "");
                        sb.append(strLine);
                    }
                }
                
            } catch (IOException e) {
                ErrorLogger EL = new ErrorLogger();
                EL.execute(e);
                logger.severe(PLAYER_FILE_READ_ERR);
                return null;
                
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }

            list.add(sb.toString().toLowerCase());
        }

        logger.info("Number of files read: " + list.size());
        
        return list;
    }
}
