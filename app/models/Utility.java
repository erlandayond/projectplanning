package models;

import java.util.Calendar;
import java.util.Date;

public class Utility {

	/**
	 * Get current week number in current year
	 * @return current week number
	 */

	public static int getCurrentWeek(){
		Date objDate=new Date();
		
		
		Calendar objCalendar=Calendar.getInstance();
		//objCalendar.set(Calendar.WEEK_OF_YEAR, 10); - for testing
		int nWeekNum=objCalendar.get(Calendar.WEEK_OF_YEAR);
		return nWeekNum;
	}
}
