package commontool;

import java.util.Calendar;
import java.util.Date;

public class DateOperation {

	public DateOperation() {
		// TODO Auto-generated constructor stub
	}

	public Integer getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public Integer getWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	public Integer getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}

	public Date changeDay(Date date, Integer index) {
		Date result = new Date();
		result = (Date) date.clone();
		Long add = (long) (index * 1000.0 * 24 * 60 * 60);
		result.setTime(date.getTime() + add);
		return result;
	}

	public Date changeExchangeDay(Date date, Integer index) {

		DateOperation self = new DateOperation();

		for (int i = 0; i < Math.abs(index); i++) {
			date = self.changeDay(date, Math.abs(index) / index);
			if (self.getDay(date) == 1 && index < 0) {
				Integer gap = -2;
				date = self.changeDay(date, gap);
			} else if (self.getDay(date) == 7 && index < 0) {
				Integer gap = -1;
				date = self.changeDay(date, gap);
			} else if (self.getDay(date) == 1 && index > 0) {
				Integer gap = 1;
				date = self.changeDay(date, gap);
			} else if (self.getDay(date) == 7 && index > 0) {
				Integer gap = 2;
				date = self.changeDay(date, gap);
			}
		}
		return date;
	}

	public static Boolean ifSameDate(Calendar date1, Calendar date2) {
		return ((date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR))
				&& (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)) && (date1
					.get(Calendar.DATE) == date2.get(Calendar.DATE))) ? true
				: false;
	}
	
	public static Boolean ifWeekend(Calendar date1) {
		return (date1.get(Calendar.DAY_OF_WEEK) == 1 || date1.get(Calendar.DAY_OF_WEEK) == 7) ? true
				: false;
	}
}
