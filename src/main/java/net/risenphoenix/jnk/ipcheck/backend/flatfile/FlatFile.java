package net.risenphoenix.jnk.ipcheck.backend.flatfile;

import net.risenphoenix.jnk.ipcheck.backend.Backend;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.Language;
import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;
import org.bukkit.Bukkit;

public class FlatFile implements Backend{

    // BACKEND MANAGER FOR FLAT-FILE - Last Updated May 25, 2013
    private static final Logger logger = Bukkit.getLogger();
    private static String registrar = "Flat-File Backend Manager for IP-Check ver 3.0";

    // Files
    private static File bannedIPs = new File("banned-ips.txt");

    // Folders
    private static File dirRoot = new File("plugins/IP-check/DATABASE");
    private static File dirPlayer = new File("plugins/IP-check/DATABASE/PLAYERS");
    private static File dirIP = new File("plugins/IP-check/DATABASE/IPS");

    @Override
    public void onLoad() {
        initializeBackend();
    }

    @Override
    public void onDisable() {
        // Not used
    }

    @Override
    public void initializeBackend() {
        if (!dirRoot.exists()) {
            dirRoot.mkdir();
        }

        if (!dirPlayer.exists()) {
            dirPlayer.mkdir();
        }

        if (!dirIP.exists()) {
            dirIP.mkdir();
        }
    }

    @Override
    public String getRegistrar() {
        return registrar;
    }

    @Override
    public ArrayList<String> loadFile(File file) {
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader br = null;

        // FILE READER
        try {
            // Open a file stream to read the file
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                list.add(strLine.toLowerCase()); // Add each line of the file to memory
            }

        } catch (Exception e) {
            list = null; // Set list to null so that the calling method can deal with the error
            
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger(); // Catch the error, pass to handler
                EL.execute(e);
                logger.severe(e.getMessage());
            }
        }
        // END FILE READER
        
        return list;
    }

    @Override
    public void log(String player, String ip) {
        // Generate the File Path for the player log
        File playerFile = new File("plugins/IP-check/DATABASE/PLAYERS/" + player + ".log");
        
        // Convert IP from XXX.XXX.XXX.XXX to XXX_XXX_XXX_XXX
        String convertedIP = convertIPFormat(ip);
        
        // Generate the File Path for the ip log
        File ipFile = new File("plugins/IP-check/DATABASE/IPS/" + convertedIP + ".log");
        
        //### IP-LOG SECTOR ###//
        if (!ipFile.exists()) {
            
            // FILE WRITER
            FileWriter f = null;
		
            try {
                f = new FileWriter(ipFile, true);
                f.write(player + "\r\n"); //Store the player name passed to the log file.
                
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                EL.execute(e);
                logger.severe(Language.FLAT_FILE_GEN_ERR);
                
            } finally {
                try {
                    if (f != null) {
                        f.close(); // Close the file writer
                    }
                } catch (Exception e){
                    ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                    EL.execute(e);
                    logger.severe(e.getMessage());
                }
            }
            // END FILE WRITER
            
        // If the file does exist, read it into memory
        } else {
            ArrayList<String> list = new ArrayList<String>();
            BufferedReader br = null;
            boolean shouldWrite = true;

            // FILE READER
            try {
                // Open a file stream to read the file
                FileInputStream fstream = new FileInputStream(ipFile);
                DataInputStream in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                while ((strLine = br.readLine()) != null) {
                    list.add(strLine.toLowerCase()); // Add each line of the file to memory
                }

            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                EL.execute(e);
                logger.severe(Language.FLAT_FILE_READ_ERR);
                list = null; // If we encountered an error reading the file, set the memory location to NULL so that we can handle the error.

            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (Exception e) {
                    ErrorLogger EL = new ErrorLogger(); // Catch the error, pass to handler
                    EL.execute(e);
                    logger.severe(e.getMessage());
                }
            }
            // END FILE READER
            
            for (String s:list) {
                if (s.equals(player.toLowerCase())) {
                    shouldWrite = false; // If the file already exists and the IP is already logged, do not write the log file
                }
            }
            
            // If we did not find the IP
            if (shouldWrite) {
                list.add(player); // Add the IP to the file's memory block

                // Write the log file to disk

                // FILE WRITER
                FileWriter f = null;

                try {
                    if (ipFile.exists()) ipFile.delete(); // Delete the old log file

                    f = new FileWriter(ipFile, true);

                    for (String s:list) {
                        f.write(s + "\r\n"); //Write each line in memory to the file, plus a line break and carriage return.
                    }

                } catch (Exception e) {
                    ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                    EL.execute(e);
                    logger.severe(Language.FLAT_FILE_WRITE_ERR);

                } finally {
                    try {
                        if (f != null) {
                            f.close(); // Close the file writer
                        }
                    } catch (Exception e){
                        ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                        EL.execute(e);
                        logger.severe(e.getMessage());
                    }
                }
                // END FILE WRITER
            }
        }
        
        //### PLAYER-LOG SECTOR ###/
        // If the player log path does not exist, generate it
        if (!playerFile.exists()) {
            
            // FILE WRITER
            FileWriter f = null;
		
            try {
                f = new FileWriter(playerFile, true);
                f.write(ip + "-lastknown\r\n"); //Store the IP-Address passed to the log file.
                
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                EL.execute(e);
                logger.severe(Language.FLAT_FILE_GEN_ERR);
                
            } finally {
                try {
                    if (f != null) {
                        f.close(); // Close the file writer
                    }
                } catch (Exception e){
                    ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                    EL.execute(e);
                    logger.severe(e.getMessage());
                }
            }
            // END FILE WRITER
            
        // If the file does exist, read it into memory
        } else {
            ArrayList<String> list = new ArrayList<String>();
            BufferedReader br = null;

            // FILE READER
            try {
                // Open a file stream to read the file
                FileInputStream fstream = new FileInputStream(playerFile);
                DataInputStream in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                while ((strLine = br.readLine()) != null) {
                    list.add(strLine.toLowerCase()); // Add each line of the file to memory
                }

            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                EL.execute(e);
                logger.severe(Language.FLAT_FILE_READ_ERR);
                list = null; // If we encountered an error reading the file, set the memory location to NULL so that we can handle the error.

            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (Exception e) {
                    ErrorLogger EL = new ErrorLogger(); // Catch the error, pass to handler
                    EL.execute(e);
                    logger.severe(e.getMessage());
                }
            }
            // END FILE READER
            
            boolean shouldAdd = true;
            
            for (String s:list) {
                if (s.equals(ip + "-lastknown")) {
                    return; // If the file already exists and the IP is already logged, we're done.
                } else if (s.equals(ip)) {
                    shouldAdd = false; // We should not log a new entry, we should modify an existing entry
                    break;
                }
            }
            
            // If we did not find the IP
            if (shouldAdd) {
                list.add(ip + "-lastknown"); // Add the IP to the file's memory block
            } else if (!shouldAdd) {
                list.remove(ip);
                list.add(ip + "-lastknown");
            }
            
            // Write the log file to disk
            
            // FILE WRITER
            FileWriter f = null;
		
            try {
                if (playerFile.exists()) playerFile.delete(); // Delete the old log file
                
                f = new FileWriter(playerFile, true);
                
                for (String s:list) {
                    if (s.contains("-lastknown") && !s.equals(ip + "-lastknown")) {
                        s = s.replace("-lastknown", ""); // If this is not the latest IP, then remove the -lastKnown tag
                    }
                    
                    f.write(s + "\r\n"); //Write each line in memory to the file, plus a line break and carriage return.
                }
                
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                EL.execute(e);
                logger.severe(Language.FLAT_FILE_WRITE_ERR);
                
            } finally {
                try {
                    if (f != null) {
                        f.close(); // Close the file writer
                    }
                } catch (Exception e){
                    ErrorLogger EL = new ErrorLogger(); // Catch the exception and pass it to the error logger
                    EL.execute(e);
                    logger.severe(e.getMessage());
                }
            }
            // END FILE WRITER
        }
    }

    @Override
    public ArrayList<String> getAlts(String ip) {
        // Convert IP from XXX.XXX.XXX.XXX to XXX_XXX_XXX_XXX
        String convertedIP = convertIPFormat(ip);
        // Create the file path to load
        File ipPath = new File("plugins/IP-check/DATABASE/IPS/" + convertedIP + ".log");
        // Load the file path
        ArrayList<String> players = loadFile(ipPath);
        
        return players;
    }

    @Override
    public boolean isBannedIP(String ip) {
       BufferedReader br = null;
		
        try {
            FileInputStream fstream = new FileInputStream(bannedIPs);
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            /* Skip the first three lines of the file to prevent the while statement
            *  from terminating prematurely. */
            for (int lineSkip = 0; lineSkip < 3; lineSkip++) br.readLine(); 

            while ((strLine = br.readLine()) != null) {
                if (strLine.contains(ip)) {
                        return true;
                }
            }
        } catch (Exception e) {
            ErrorLogger EL = new ErrorLogger();
            EL.execute(e);
            logger.severe(Language.BAN_LIST_READ_ERR);
        } finally {
            try {
                if (br != null) {
                        br.close();
                }
            } catch (Exception e) {
                ErrorLogger EL = new ErrorLogger();
                EL.execute(e);
                logger.severe(e.getMessage());
            }
        }

        return false;
    }

    @Override
    public String getLastKnownIP(String player) {
        ArrayList<String> IPs = loadFile(new File("plugins/IP-check/DATABASE/PLAYERS/" + player.toLowerCase() + ".log"));
        
        for (String s:IPs) {
            if (s.contains("-lastknown")) {
                return s.replace("-lastknown", "");
            }
        }
        
        return "0.0.0.0";
    }
    
    @Override
    public ArrayList<String> getIPs(String player) {
        ArrayList<String> IPs = loadFile(new File("plugins/IP-check/DATABASE/PLAYERS/" + player.toLowerCase() + ".log"));
        
        if (IPs == null) {
            return null;
        }
        
        return IPs;
    }
    
    @Override
    public String checkIPaddress(String ip) {
        ArrayList<String> check = loadFile(new File("plugins/IP-check/DATABASE/IPS/" + convertIPFormat(ip) + ".log"));
        if (check == null) {
            return "no-find";
        }
        
        return ip;
    }
    
    // Backend Specific
    // Converts IP-Address passed from XXX.XXX.XXX.XXX to XXX_XXX_XXX_XXX
    public String convertIPFormat(String ip) {
        StringBuilder convertedIP = new StringBuilder();
        for (int i = 0; i < ip.length(); i++) {
            if (ip.charAt(i) != '.') {
                convertedIP.append(ip.charAt(i));
            } else {
                convertedIP.append('_');
            }
        }
        
        return convertedIP.toString();
    }
}
