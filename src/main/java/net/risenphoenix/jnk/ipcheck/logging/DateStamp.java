package net.risenphoenix.jnk.ipcheck.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.risenphoenix.jnk.ipcheck.configuration.ConfigurationManager;

public class DateStamp {

	public String getDateStamp() {
		DateFormat dateFormat = new SimpleDateFormat(ConfigurationManager.dateStampFormat);

       //get current date time with Calendar()
       Calendar cal = Calendar.getInstance();
       return dateFormat.format(cal.getTime());
	}
	
	public String getErrorStamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

       //get current date time with Calendar()
       Calendar cal = Calendar.getInstance();
       return dateFormat.format(cal.getTime());
	}
	
	public String getCustomStamp(String regex) {
		DateFormat dateFormat = new SimpleDateFormat(regex);

       //get current date time with Calendar()
       Calendar cal = Calendar.getInstance();
       return dateFormat.format(cal.getTime());
	}
	
}
