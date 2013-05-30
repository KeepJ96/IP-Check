package net.risenphoenix.jnk.ipcheck.database;
import java.sql.*;
import net.risenphoenix.jnk.ipcheck.IPcheck;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
/**
 *
 * @author FR34KYN01535
 */
public class DatabaseManager{

    private static final Logger logger = Bukkit.getLogger();
    
    private JDBCConnection connection;
    private String type="";

    public DatabaseManager(boolean useMySQL){
    if(useMySQL){
        connection = new JDBCConnection(IPcheck.Configuration.dbHostname, 
                                        IPcheck.Configuration.dbPort,
                                        IPcheck.Configuration.dbName,
                                        IPcheck.Configuration.dbUsername,
                                        IPcheck.Configuration.dbPassword);
        type="mysql";
    }else{
        connection = new JDBCConnection();
        type="sqlite";
    } 
        
        if(!IPcheck.getInstance().getConfig().getBoolean("dbGenerated")){
                connection.query("DROP TABLE IF EXISTS ipcheck_log;");
                connection.query("DROP TABLE IF EXISTS ipcheck_player;");
                connection.query("DROP TABLE IF EXISTS ipcheck_ip;");
                if(type.equals("mysql")){
                connection.query("CREATE TABLE ipcheck_log ( "+
                                    "id int(11) NOT NULL AUTO_INCREMENT,"+
                                    "ip varchar(11) NOT NULL,"+
                                    "timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "username varchar(255) NOT NULL,"+
                                    "PRIMARY KEY (id)"+
                                    ");");
                connection.query("CREATE TABLE ipcheck_user ( "+
                                    "username varchar(255) NOT NULL,"+
                                    "timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "banmessage varchar(255) NOT NULL,"+
                                    "banned bit(1) NOT NULL DEFAULT b'0',"+
                                    "exempted bit(1) NOT NULL DEFAULT b'0',"+
                                    "PRIMARY KEY (username)"+
                                    ");");
                connection.query("CREATE TABLE ipcheck_ip ( "+
                                    "ip varchar(11) NOT NULL,"+
                                    "timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "banned bit(1) NOT NULL DEFAULT b'0',"+
                                    "exempted bit(1) NOT NULL DEFAULT b'0',"+
                                    "PRIMARY KEY (ip)"+
                                    ");");
                }else{
                connection.query("CREATE TABLE ipcheck_log ( "+
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"+
                                    "ip TEXT NOT NULL,"+
                                    "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "username TEXT NOT NULL"+
                                    ");");
                connection.query("CREATE TABLE ipcheck_user ( "+
                                    "username TEXT NOT NULL ,"+
                                    "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "banmessage TEXT NOT NULL ,"+
                                    "banned INTEGER NOT NULL DEFAULT 0,"+
                                    "exempted INTEGER NOT NULL DEFAULT 0"+
                                    ");");
                connection.query("CREATE TABLE ipcheck_ip ( "+
                                    "ip TEXT NOT NULL ,"+
                                    "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
                                    "banned INTEGER NOT NULL DEFAULT 0,"+
                                    "exempted INTEGER NOT NULL DEFAULT 0"+
                                    ");");
                }
        }
    }
    public void close(){
        connection.close();
    }
    public void log(String player, String ip) {
            connection.query("insert into ipcheck_log (ip,username) values ('"+ip+"','"+player+"');");
    }
    
    public boolean banPlayer(String player,String message) {
            if(connection.query("update ipcheck_user set banned=1,banmessage='"+message+"' where username='"+player+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean unbanPlayer(String player) {
            if(connection.query("update ipcheck_user set banned=0 where username='"+player+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean isBannedPlayer(String player) {
        try {
            ResultSet res = connection.query("select banned from ipcheck_user where username='"+player+"'").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    int banned = Integer.parseInt(res.getString(1));
                    if(banned==1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String getBanMessage(String player) {
        String returning="";
        try {
            ResultSet res =  connection.query("SELECT banmessage FROM ipcheck_user where username='"+player+"';").getResultSet();
       
            if(res.next()){
                returning = res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returning;
    }
    
    
    public boolean exemptPlayer(String player) {
            if(connection.query("update ipcheck_user set exempted=1 where username='"+player+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean unexemptPlayer(String player) {
            if(connection.query("update ipcheck_user set exempted=0 where username='"+player+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean isExemptedPlayer(String player) {
        try {
            ResultSet res = connection.query("select exempted from ipcheck_user where username='"+player+"'").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    int exempted = Integer.parseInt(res.getString(1));
                    if(exempted==1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean banIP(String ip) {
            if(connection.query("update ipcheck_ip set banned=1 where ip='"+ip+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean unbanIP(String ip) {
            if(connection.query("update ipcheck_ip set banned=0 where ip='"+ip+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean isBannedIP(String ip) {
        try {
            ResultSet res = connection.query("select banned from ipcheck_ip where ip='"+ip+"'").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    int banned = Integer.parseInt(res.getString(1));
                    if(banned==1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean exemptIP(String ip) {
            if(connection.query("update ipcheck_ip set exempted=1 where ip='"+ip+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean unexemptIP(String ip) {
            if(connection.query("update ipcheck_ip set exempted=0 where ip='"+ip+"'")==null){
                return false;
            }else{
                return true;
            }
    }
    
    public boolean isExemptedIP(String ip) {
        try {
            ResultSet res = connection.query("select exempted from ipcheck_ip where ip='"+ip+"'").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    int exempted = Integer.parseInt(res.getString(1));
                    if(exempted==1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public ArrayList<String> getPlayerExemptList() {
        ArrayList<String> names = new ArrayList<String>();
        try {
            ResultSet res =  connection.query("select username from ipcheck_user where exempted=1").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    names.add(res.getString(1));
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return names;
    }
    
     public ArrayList<String> getIpExemptList() {
        ArrayList<String> ips = new ArrayList<String>();
        try {
            ResultSet res =  connection.query("select ip from ipcheck_ip where exempted=1").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    ips.add(res.getString(1));
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ips;
    }
    
    public ArrayList<String> getAlts(String ip) {
        ArrayList<String> ips = new ArrayList<String>();
        try {
            ResultSet res =  connection.query("select username from ipcheck_log where ip='"+ip+"'").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    ips.add(res.getString(1));
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ips;
    }
    
    public ArrayList<String> getIPs(String player) {
        ArrayList<String> ips = new ArrayList<String>();
        try {
            ResultSet res =  connection.query("select ip from ipcheck_log where username='"+player+"'").getResultSet();
            if(res.getMetaData().getColumnCount()==1){
                while(res.next()){
                    ips.add(res.getString(1));
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ips;
    }
    
    public String getLastKnownIP(String player) {
        String returning="";
        try {
            ResultSet res =  connection.query("SELECT ip FROM ipcheck_log where username='"+player+"' order by timestamp desc limit 1;").getResultSet();
       
            if(res.next()){
                returning = res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returning;
    }
    
    public String checkIPaddress(String ip) {     
        String returning="";
        try {
            ResultSet res =  connection.query("select ip from ipcheck_log where ip='"+ip+"'").getResultSet();
       
            if(res.next()){
                returning = res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (ip.equals(returning)) {
            return ip;
        }else{
            return "no-find";
        }
    }
}
