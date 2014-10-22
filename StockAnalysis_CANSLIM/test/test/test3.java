package test;

import java.util.ArrayList;

import tool.charDeal;
import dao.UrlDao;

public class test3 {

	public static void main(String args[]) {
		Integer years = 0;
		// ArrayList<String> result = UrlDao
		// .getUrlBuffer("https://au.finance.yahoo.com/q/is?s=AA&annual");
		ArrayList<String> result = UrlDao
				.getUrlBuffer("https://au.finance.yahoo.com/q/bs?s=AA&annual");
		Boolean ifHasResult = true;
		for (String ele : result) {
			if (ele.contains("The document has moved")
					|| ele.contains("There is no Income Statement data")) {
				System.out.println("No Income Statemeent data!");
				ifHasResult = false;
			}
		}
		if (ifHasResult == true) {
			System.out.println(result.size());
			for (int j = 0; j < result.size(); j++) {
				if (result.get(j).contains("Period Ending")) {
					System.out.println("Period Ending:");
					String[] hi = result.get(j).split("bold\">");
					for (int i = 1; i < hi.length; i++) {
						System.out.println(hi[i].substring(0,
								hi[i].indexOf("</th>"))
								+ " ");
						years++;
					}
				}
			}
			String[] itemList = { "Cash And Cash Equivalents", "Short Term Investments",
					"Net Receivables", "Inventory",
					"Other Current Assets", "Total Current Assets",
					"Long Term Investments", "Property Plant and Equipment",
					"Goodwill",
					"Intangible Assets",
					"Accumulated Amortisation", "Other Assets",
					"Deferred Long Term Asset Charges", "Total Assets",
					"Accounts Payable", "Short/Current Long Term Debt",
					"Other Current Liabilities", "Total Current Liabilities",
					"Long Term Debt", "Other Liabilities",
					"Deferred Long Term Liability Charges", "Minority Interest",
					"Negative Goodwill","Total Liabilities","Misc Stocks Options Warrants",
					"Redeemable Preferred Stock","Preferred Stock",
					"Common Stock","Retained Earnings",
					"Treasury Stock","Capital Surplus",
					"Other Stockholder Equity","Total Stockholder Equity",
					"Net Tangible Assets"};
			for (String item : itemList) {
				findValue(item, result, years);
			}
		}

	}

	public static void findValue(String item, ArrayList<String> result,
			Integer years) {
		String mark = "right";
		for (int j = 0; j < result.size(); j++) {
			if (result.get(j).contains(item)) {
				System.out.println(item + " :");
				String tempString = "";
				Integer flag = 0;
				while (flag < years) {
					// System.out.println(result.get(j).trim());
					flag += charDeal.countMark(result.get(j), mark);
					// System.out.println(flag);
					tempString += result.get(j).trim();
					j++;
				}
				tempString = tempString.substring(tempString.indexOf(item));
				while (!tempString.contains("</td></tr><tr>")) {
					tempString += result.get(j).trim();
					j++;
				}
				if (tempString.contains("</td></tr><tr>")) {
					tempString = tempString.substring(0,
							tempString.indexOf("</td></tr><tr>"));
				}
				String[] stringList = tempString.split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = stringList[i];
					if (charDeal.hasDigital(output)) {
						if (output.contains("(")) {
							output = "-" + charDeal.extractDigital(output);
						} else {
							output = charDeal.extractDigital(output);
						}
					} else {
						output = "null";
					}
					System.out.println(output);
				}
				break;
			}
		}
	}
}
