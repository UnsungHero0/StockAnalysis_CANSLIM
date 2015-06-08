package test;

import java.util.ArrayList;
import java.util.Calendar;

import dao.DateDao;
import dao.UrlDao;
import tool.consolePrint;

public class test6 {
	public static void main(String[] args) {
		String code = "1ST";
		String csvUrl = "http://real-chart.finance.yahoo.com/table.csv?s="
				+ code + ".AX&a=00&b=1&c=1980&" + "d="
				+ (Integer.valueOf(DateDao.month) - 1) + "&" + "e="
				+ (DateDao.day) + "&" + "f=" + (DateDao.year) + "&"
				+ "g=d&ignore=.csv";
		ArrayList<String> urlResult = UrlDao.getUrlBuffer(csvUrl);
		System.out.println(urlResult.size() == 0);
		for (String ele : urlResult) {
			System.out.println(urlResult.size() == 0);
		}
	}
}
