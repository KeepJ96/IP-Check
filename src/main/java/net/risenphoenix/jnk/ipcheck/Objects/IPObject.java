package net.risenphoenix.jnk.ipcheck.Objects;

import java.util.ArrayList;

public class IPObject {

    private String IP;
    private ArrayList<String> Users;
 
    public IPObject(String IP, ArrayList<String> Users) {
        this.IP = IP;
        this.Users = Users;
    }
 
    public int getNumberOfUsers() {
        return this.Users.size();
    }
 
    public String getIP() {
        return this.IP;
    }
 
    public ArrayList<String> getUsers() {
        return this.Users;
    }
    
}
