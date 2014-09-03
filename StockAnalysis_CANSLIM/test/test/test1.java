package test;

import java.util.ArrayList;

import dao.UrlDao;

public class test1 {

	public static void main(String args[]) {
		ArrayList<String> result = UrlDao
				.getUrlBuffer("http://real-chart.finance.yahoo.com/table.csv?s=3PL.AX&a=00&b=1&c=2010&d=08&e=2&f=2014&g=d&ignore=.csv");
		System.out.println(result.size());
		for (String ele : result) {
			for (String elee : ele.split(",")) {
				System.out.print(elee + "   ");
			}
			System.out.println();
		}
	}

}
