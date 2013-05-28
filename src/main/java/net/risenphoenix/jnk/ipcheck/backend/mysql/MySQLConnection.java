package net.risenphoenix.jnk.ipcheck.backend.mysql;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
/**
 *
 * @author FR34KYN01535
 */
public class MySQLConnection extends Database 
{ 
    String user = ""; 
    String database = ""; 
    String password = ""; 
    int port = 3306; 
    String hostname = ""; 
    Connection c = null; 
  
  
    public MySQLConnection(String hostname, int portnmbr, String database, String username, String password) { 
        this.hostname = hostname; 
        this.port = portnmbr; 
        this.database = database; 
        this.user = username; 
        this.password = password; 
    } 
    public Connection open() { 
        try { 
            Class.forName("com.mysql.jdbc.Driver"); 
            this.c = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password); 
            return c; 
        } catch (SQLException e) { 
            System.out.println("Could not connect to MySQL server! because: " + e.getMessage()); 
        } catch (ClassNotFoundException e) { 
            System.out.println("JDBC Driver not found!"); 
        } 
        return this.c; 
    } 
    public boolean checkConnection() { 
        if (this.c != null) { 
            return true; 
        } 
        return false; 
    } 
    public Connection getConn() { 
        return this.c; 
    } 
    public void closeConnection(Connection c) { 
        c = null; 
    } 
} 