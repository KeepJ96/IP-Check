package net.risenphoenix.jnk.ipcheck.Objects;

import java.util.ArrayList;

public class UserObject {

    private String User;
    private ArrayList<String> IPs;
    
    public UserObject(String User, ArrayList<String> IPs) {
        this.User = User;
        this.IPs = IPs;
    }
    
    public String getUser() {
        return this.User;
    }
    
    public ArrayList<String> getIPs() {
        return this.IPs;
    }
    
    public int getNumberOfIPs() {
        return this.IPs.size();
    }
    
}
