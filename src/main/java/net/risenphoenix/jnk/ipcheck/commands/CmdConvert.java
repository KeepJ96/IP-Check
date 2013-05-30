package net.risenphoenix.jnk.ipcheck.commands;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import net.risenphoenix.jnk.ipcheck.logging.ErrorLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CmdConvert implements IpcCommand{

    private static final Logger logger = Bukkit.getLogger();
    
    private static File dirPlayer = new File("plugins/IP-check/DATABASE/PLAYERS");
    private static File playersDir = new File("plugins/Essentials/userdata");
    
    public void execute(CommandSender sender, String commandLabel, String[] args) {
        ArrayList<String> load;
        if (args[1].equals("-e")) {
            load=loadEssentials();
        }else{
            load=loadPlainText();
        }
        
        for (String s:load) {
            String[] sArray = s.split("|");
            IPcheck.Database.log(sArray[0],sArray[1]);
        }
    }
    
    public ArrayList<String> loadEssentials() {
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
                logger.severe("Error occurred while attempting to read essentials player file!");
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
    
    public static ArrayList<String> loadPlainText() {
        ArrayList<String> manifest = new ArrayList<String>();
            File[] players = dirPlayer.listFiles();
       
        StringBuilder sb = new StringBuilder();
       
        for (File f:players) {
            String path = f.getAbsolutePath();
            path = path.replace(System.getProperty("user.dir"), "");
            path = path.replace("\\plugins\\IP-check\\DATABASE\\PLAYERS\\", "");
            path = path.replace(".log", "");
            sb.append(path + "|");
           
            ArrayList<String> list = loadFile(f);
           
            for (String s:list) {
                if (s.contains("-lastknown")) {
                    sb.append(s.replace("-lastknown", ""));
                    break;
                }
            }
           
            manifest.add(sb.toString());
            Bukkit.getLogger().info(sb.toString());
            sb = new StringBuilder();
        }
       
        return manifest;
    }
    
    public static ArrayList<String> loadFile(File file) {
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

    public int getID() {
        return 13;
    }

    public String getHelp() {
        return "Converts old database formats into a new database.";
    }

    public String getSyntax() {
        return "convert";
    }

    public Permission[] getPermissions() {
        Permission perms[] = {
            new Permission("ipcheck.convert")
        };

        return perms;
    }

    public String getName() {
        return "Convert";
    }

}
