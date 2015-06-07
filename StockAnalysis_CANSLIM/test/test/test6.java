package test;

import java.util.Calendar;

import tool.consolePrint;

public class test6 {
	public static void main(String[] args) {
		Calendar date = Calendar.getInstance();
		consolePrint.println(date.get(Calendar.YEAR) + "-"
				+ (date.get(Calendar.MONTH) + 1) + "-"
				+ date.get(Calendar.DATE));
	}
}
