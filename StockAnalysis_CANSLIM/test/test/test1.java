package test;

import java.util.ArrayList;

import tool.charDeal;
import dao.UrlDao;

public class test1 {

	public static void main(String args[]) {
		Integer years = 3;
		//ArrayList<String> result = UrlDao
			//	.getUrlBuffer("https://au.finance.yahoo.com/q/is?s=AA&annual");
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
			} else if (result.get(j).contains("Total Revenue")) {
				System.out.println(result.get(j).trim() + ":");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (charDeal.hasDigital(result.get(j))) {
						System.out.println(charDeal.extractDigital(result
								.get(j)));
						flag++;
					}
				}
			} else if (result.get(j).contains("Cost of Revenue")) {
				System.out.println("Cost of Revenue :");
				String[] stringList = result.get(j).split("</td>");
				stringList = stringList[1].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]
							.substring(0, stringList[i].indexOf("&nbsp")));
					if (output.contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Gross Profit")) {
				System.out.println(result.get(j).trim() + ":");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			} else if (result.get(j).contains("Research Development")) {
				System.out.println("Research Development :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains(
					"Selling General and Administrative")) {
				System.out.println("Selling General and Administrative :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Non Recurring")) {
				System.out.println("Non Recurring :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Others")) {
				System.out.println("Others :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Total Operating Expenses")) {
				System.out.println("Total Operating Expenses :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Operating Income or Loss")) {
				System.out.println(result.get(j).trim() + " :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			} else if (result.get(j)
					.contains("Total Other Income/Expenses Net")) {
				System.out.println("Total Other Income/Expenses Net :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains(
					"Earnings Before Interest And Taxes")) {
				System.out.println("Earnings Before Interest And Taxes :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Interest Expense")) {
				System.out.println("Interest Expense :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Income Before Tax")) {
				System.out.println("Income Before Tax :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Income Tax Expense")) {
				System.out.println("Income Tax Expense :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Minority Interest")) {
				System.out.println("Minority Interest :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Net Income From Continuing Ops")) {
				System.out.println("Net Income From Continuing Ops :");
				String[] stringList = result.get(j).split("</td></tr>");
				stringList = stringList[0].split("<td align=\"right\">");
				for (int i = 1; i < stringList.length; i++) {
					String output = charDeal.extractDigital(stringList[i]);
					if (stringList[i].contains("(")) {
						System.out.println("-" + output);
					} else {
						System.out.println(output);
					}
				}
			} else if (result.get(j).contains("Discontinued Operations")) {
				System.out.println( "Discontinued Operations :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			}  else if (result.get(j).contains("Extraordinary Items")) {
				System.out.println("Extraordinary Items :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			}  else if (result.get(j).contains("Effect Of Accounting Changes")) {
				System.out.println("Effect Of Accounting Changes :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			}  else if (result.get(j).contains("Other Items")) {
				System.out.println("Other Items :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			} else if (result.get(j).contains("Net Income") && !result.get(j).contains("Applicable")) {
				System.out.println("Net Income :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			} else if (result.get(j).contains("Preferred Stock And Other Adjustments")) {
				System.out.println("Preferred Stock And Other Adjustments :");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			} else if (result.get(j).contains("Net Income Applicable To Common Shares")) {
				System.out.println("Net Income Applicable To Common Shares:");
				Integer flag = 0;
				while (flag < years) {
					j++;
					if (result.get(j).contains("-")) {
						System.out.println("-");
						flag++;
					} else if (charDeal.hasDigital(result.get(j))) {
						if (result.get(j).contains("(")) {
							System.out.println("-"
									+ charDeal.extractDigital(result.get(j)));
						} else {
							System.out.println(charDeal.extractDigital(result
									.get(j)));
						}
						flag++;
					}
				}
			} 
		}
	}

}
