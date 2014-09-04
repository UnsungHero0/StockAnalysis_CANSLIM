package test;

import java.util.ArrayList;

import dao.UrlDao;

public class test1 {

	public static void main(String args[]) {
		ArrayList<String> result = UrlDao
				.getUrlBuffer("http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=NASDAQ&render=download");
		System.out.println(result.size());
		for (String ele : result) {
			for (String elee : ele.split("\",")) {
				System.out.print(elee + " ");
			}
			System.out.println();
		}
	}

}
