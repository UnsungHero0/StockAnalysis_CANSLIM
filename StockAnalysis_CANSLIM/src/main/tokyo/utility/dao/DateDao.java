package dao;

import java.util.Calendar;

public class DateDao {

	public final static String year = String.valueOf(Calendar.getInstance()
			.get(Calendar.YEAR));
	public final static String month = String.valueOf(Calendar.getInstance()
			.get(Calendar.MONTH) + 1);
	public final static String day = String.valueOf(Calendar.getInstance()
			.get(Calendar.DATE));

	public static String dateTodayInMysqlForm() {
		return year + "/" + month + "/" + day;
	}
	
	public static String dateIntoMysqlForm(Calendar date) {
		String month = (date.get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = (date.get(Calendar.DAY_OF_MONTH)) + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		String year = date.get(Calendar.YEAR) + "";
		return year + "/" + month + "/" + day; 
	}

}
