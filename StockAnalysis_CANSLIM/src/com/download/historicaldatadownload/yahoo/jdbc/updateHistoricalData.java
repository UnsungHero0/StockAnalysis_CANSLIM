package com.download.historicaldatadownload.yahoo.jdbc;

import java.util.Calendar;

public class updateHistoricalData {
	
	private final static String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	private final static String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
	private final static String day = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
	private final static String startYear = "1983";
	private final static String startMonth = "1";
	private final static String StartDay = "1";

	public updateHistoricalData() {
		// TODO Auto-generated constructor stub
	}

	public static String getYear() {
		return year;
	}

	public static String getMonth() {
		return month;
	}

	public static String getDay() {
		return day;
	}

	public static String getStartyear() {
		return startYear;
	}

	public static String getStartmonth() {
		return startMonth;
	}

	public static String getStartday() {
		return StartDay;
	}

	public static void main(String args[]) {
		
		
		System.out.println(getYear());
		System.out.println(getMonth());
		System.out.println(getDay());
		
		
	}
	
	

}
