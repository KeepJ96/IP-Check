package net.risenphoenix.jnk.ipcheck.backend.mysql;
import java.sql.*;
import net.risenphoenix.jnk.ipcheck.backend.Backend;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
/**
 *
 * @author FR34KYN01535
 */
public class MySQL implements Backend{

    private static final Logger logger = Bukkit.getLogger();
    private static String registrar = "MySQL Backend Manager for IP-Check ver 1.0";
    
    private MySQLConnection connection;
    private Connection c;
    
    @Override
    public void onLoad() {
        initializeBackend();
    }

    @Override
    public void onDisable() {
        connection.closeConnection(c);
    }

    @Override
    public void initializeBackend() {
        connection = new MySQLConnection(IPcheck.Configuration.dbHostname, 
                                        IPcheck.Configuration.dbPort,
                                        IPcheck.Configuration.dbName,
                                        IPcheck.Configuration.dbUsername,
                                        IPcheck.Configuration.dbPassword); 
        c = connection.open();
        
        if(!IPcheck.getInstance().getConfig().getBoolean("dbGenerated")){
            try {
                Statement statement = c.createStatement();
                //statement.execute("DROP TABLE IF EXISTS `players`;");
                //statement.execute("CREATE TABLE players ("+
                //                    "username varchar(255) NOT NULL,"+
                //                    "timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                //                    "PRIMARY KEY (username)"+
                //                  ");");
                statement.execute("DROP TABLE IF EXISTS log;");
                statement.execute("CREATE TABLE log ( "+
                                    "id int(11) NOT NULL AUTO_INCREMENT,"+
                                    "ip varchar(11) NOT NULL,"+
                                    "timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "username varchar(255) NOT NULL,"+
                                    "PRIMARY KEY (id)"+
                                    ");");
            } catch (SQLException ex) {
                Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getRegistrar() {
        return registrar;
    }


    @Override
    public void log(String player, String ip) {
        try {
            Statement statement = c.createStatement();
            statement.execute("insert into log (ip,username) values ('"+ip+"','"+player+"');");
            //statement.execute("INSERT INTO players (username) SELECT '"+player+"' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM players WHERE username = '"+player+"');");
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<String> getAlts(String ip) {
        ArrayList<String> ips = new ArrayList<String>();
        try {
            Statement statement = c.createStatement();
            ResultSet res =  statement.executeQuery("select username from log where ip='"+ip+"'");
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    ips.add(res.getString(1));
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ips;
    }
    
    @Override
    public ArrayList<String> getIPs(String player) {
        ArrayList<String> ips = new ArrayList<String>();
        try {
            Statement statement = c.createStatement();
            ResultSet res =  statement.executeQuery("select ip from log where username='"+player+"'");
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    ips.add(res.getString(1));
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ips;
    }
    
    @Override
    public String getLastKnownIP(String player) {
        String returning="";
        try {
            Statement statement = c.createStatement();
            ResultSet res =  statement.executeQuery("SELECT ip FROM log where username='"+player+"' order by timestamp desc limit 1;");
       
            if(res.next()){
                returning = res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returning;
    }
    
    @Override
    public String checkIPaddress(String ip) {     
        String returning="";
        try {
            Statement statement = c.createStatement();
            ResultSet res =  statement.executeQuery("select ip from log where ip='"+ip+"'");
       
            if(res.next()){
                returning = res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (ip.equals(returning)) {
            return ip;
        }else{
            return "no-find";
        }
    }
}
