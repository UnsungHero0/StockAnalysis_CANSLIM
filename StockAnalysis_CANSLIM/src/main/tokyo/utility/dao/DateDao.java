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

}
