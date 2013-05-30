IP-Check
========
Current Build: **1.3.2**  
##What is IP-Check?
IP-Check is a server plugin designed to alert you (server administrator) and your staff about players with mutliple accounts on your server. It helps to notify you of possible situations, and helps with you getting of these "multi-accounters".  
##How does IP-Check work?
In it's current state, IP-Check operates in a very simple way. Utilizing either the widely known Essentials plugin for Bukkit, or it's own backend manager, IP-Check logs every player that ever joins your server, and logs their current IP address. When the player logs in, IP-Check immediately performs a background check, comparing the IP Address of the player to it's database, and alerting you if a certain number (or higher number) of matches are found.  
With one simple command, IP-Check can provide you with a list of every account that is tied to a player, and also let you know if the person has been banned or is exempt from (background) checking.  
##How can I get IP-Check?
IP-Check is available for download on Dev Bukkit, or, if you're feeling ambitious, you can download and play with the source code on Github.  
##Current Features
In it's current state, IP-Check comes with the following features:  
   * Logging of players and their IP Addresses for the prevention and detection of mutli-accounters.  
   * Background checking of players to alert you when a multi-accounter is on your server.  
   * Exemption capabilities to allow players to go unchecked (and prevent unnecessary spam in your chat. :))  
   * Much more!  

##Upcoming Features
We've got some pretty advanced features lined up for our 1.3.x release. In fact, we're receiving the help of a few net-savvy friends in helping up with the new features. Here's some of the stuff we've got planned:  
   * MySQL Support for backend usage.  
   * A centralized network-accessable control panel for plugin/overall server management.  
   * A detailed logging system. 
   * Ability to manage players and perform plugin related tasks on them remotely from the new C-Panel.  
   * Hotdog Vendor.  
