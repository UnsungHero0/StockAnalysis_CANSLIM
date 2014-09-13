package test;

import java.util.ArrayList;

import dao.UrlDao;

public class test3 {

	public static void main(String args[]) {
		Integer years = 0;
		// ArrayList<String> result = UrlDao
		// .getUrlBuffer("https://au.finance.yahoo.com/q/is?s=AA&annual");
		ArrayList<String> result = UrlDao
				.getUrlBuffer("https://au.finance.yahoo.com/q/is?s=AAPL&annual");
		System.out.println(result.size());
		for (int j = 0; j < result.size(); j++) {
			if (result.get(j).contains("Period Ending")) {
				System.out.println("Period Ending:");
				String[] hi = result.get(j).split("bold\">");
				for (int i = 1; i < hi.length; i++) {
					System.out.println(hi[i].substring(0,
							hi[i].indexOf("</th>"))
							+ " ");
				}
				years = hi.length[]-1;
				System.out.println(years);
			}
		}
	}

}
