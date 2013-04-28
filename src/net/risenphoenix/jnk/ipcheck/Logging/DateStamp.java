package net.risenphoenix.jnk.ipcheck.Logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.risenphoenix.jnk.ipcheck.Configuration;

public class DateStamp {

	public String getDateStamp() {
		DateFormat dateFormat = new SimpleDateFormat(Configuration.dateStampFormat);

       //get current date time with Calendar()
       Calendar cal = Calendar.getInstance();
       return dateFormat.format(cal.getTime());
	}
	
}
