package quotescorrelationcoefficient;

import java.util.HashMap;

public class StockPairDailyQuoteResult extends StockPairDailyQuote{
	
	//key: different days/weeks/months/year
	//value: ArrayList 0-corcoef, 1-standard deviation
	private HashMap<Integer, StockPairDailyQuote> dayDifResult = new HashMap<>();
	private HashMap<Integer, StockPairDailyQuote> weekDifResult = new HashMap<>();
	private HashMap<Integer, StockPairDailyQuote> monthDifResult = new HashMap<>();
	private HashMap<Integer, StockPairDailyQuote> yearDifResult = new HashMap<>();
	
	public HashMap<Integer, StockPairDailyQuote> getDayDifResult() {
		return dayDifResult;
	}
	public void setDayDifResult(HashMap<Integer, StockPairDailyQuote> dayDifResult) {
		this.dayDifResult = dayDifResult;
	}
	public HashMap<Integer, StockPairDailyQuote> getWeekDifResult() {
		return weekDifResult;
	}
	public void setWeekDifResult(HashMap<Integer, StockPairDailyQuote> weekDifResult) {
		this.weekDifResult = weekDifResult;
	}
	public HashMap<Integer, StockPairDailyQuote> getMonthDifResult() {
		return monthDifResult;
	}
	public void setMonthDifResult(
			HashMap<Integer, StockPairDailyQuote> monthDifResult) {
		this.monthDifResult = monthDifResult;
	}
	public HashMap<Integer, StockPairDailyQuote> getYearDifResult() {
		return yearDifResult;
	}
	public void setYearDifResult(HashMap<Integer, StockPairDailyQuote> yearDifResult) {
		this.yearDifResult = yearDifResult;
	}
}
