package module.listedcompanydownload;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import namespace.SydneyDBNameSpace;
import tool.charDeal;
import commontool.JDBCUtil;
import dao.DBSydenyDao;
import dao.UrlDao;

public class DownLoadIncomeStatementSydeny {

	private static String[] itemList = { "Total Revenue", "Cost of Revenue",
			"Gross Profit", "Research Development",
			"Selling General and Administrative", "Non Recurring", "Others",
			"Total Operating Expenses", "Operating Income or Loss",
			"Total Other Income/Expenses Net",
			"Earnings Before Interest And Taxes", "Interest Expense",
			"Income Before Tax", "Income Tax Expense", "Minority Interest",
			"Net Income From Continuing Ops", "Discontinued Operations",
			"Extraordinary Items", "Effect Of Accounting Changes",
			"Other Items", "Net Income",
			"Preferred Stock And Other Adjustments",
			"Net Income Applicable To Common Shares" };

	private final static String CREATETABLESQL = "CREATE TABLE IF NOT EXISTS "
			+ SydneyDBNameSpace.getIncomestatementDb() + " ("
			+ "`Local_Code` VARCHAR(10) NOT NULL,"
			+ "`Section` VARCHAR(10) NOT NULL Default 'Sydney',"
			+ "`View` VARCHAR(10) NOT NULL," + "`Ending_Period` DATE NOT NULL,"
			+ "`Total_Revenue` BIGINT," + "`Cost_of_Revenue` BIGINT,"
			+ "`Gross_Profit` BIGINT," + "`Research_Development` BIGINT,"
			+ "`Selling_General_and_Administrative` BIGINT,"
			+ "`Non_Recurring` BIGINT," + "`Others` BIGINT,"
			+ "`Total_Operating_Expenses` BIGINT,"
			+ "`Operating_Income_or_Loss` BIGINT,"
			+ "`Total_Other_Income/Expenses_Net` BIGINT,"
			+ "`Earnings_Before_Interest_And_Taxes` BIGINT,"
			+ "`Interest_Expense` BIGINT," + "`Income_Before_Tax` BIGINT,"
			+ "`Income_Tax_Expense` BIGINT," + "`Minority_Interest` BIGINT,"
			+ "`Net_Income_From_Continuing_Ops` BIGINT,"
			+ "`DiscontinuedvOperations` BIGINT,"
			+ "`Extraordinary_Items` BIGINT,"
			+ "`Effect_Of_AccountingvChanges` BIGINT,"
			+ "`Other_Items` BIGINT," + "`Net_Income` BIGINT,"
			+ "`PreferredvStock_And_Other_Adjustments` BIGINT,"
			+ "`Net_Income_Applicable_To_Common_Shares` BIGINT,"
			+ "PRIMARY KEY (`Local_Code`, `View`, `Ending_Period`))";

	private static Integer groupNumber = 50;

	public static void downloadIncomeStatement(Connection con) {

		// 1. get CodeList
		ArrayList<String> codeList = DBSydenyDao.getCodeListFromDB(con);

		// 2. split codeList into 100 groups to decrease the burden of mysql
		// write burden
		ArrayList<ArrayList<String>> codeListGroup = splitCodeList(codeList);

		// 3. drop Table (if necessary)
		//dropTable(con);
		//System.out.println("Dropped Table!");

		// 4. create Table
		createTable(con);
		System.out.println("Created Table!");

		for (int i = 0; i < codeListGroup.size(); i++) {

			// 5. download data to collect value used to insert into table
			String value = "";
			for (String code : codeListGroup.get(i)) {
				value += downLoadOneIncomeStatement(code, con);
			}
			// 6. insert Data
			if (value.length() > 1) {
				value = value.substring(0, value.length() - 1);
				insertDataIntoDB(value, con);
			}
			System.out.println((i + 1) + " gourp data is inserted!("
					+ codeList.size() / groupNumber + " in all)");
		}
	}

	public static ArrayList<ArrayList<String>> splitCodeList(
			ArrayList<String> inputCodeList) {
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		for (int i = 0; i < inputCodeList.size(); i++) {
			ArrayList<String> oneGroup = new ArrayList<>();
			for (int j = 0; j < groupNumber && i < inputCodeList.size(); j++, i++) {
				oneGroup.add(inputCodeList.get(i));
			}
			result.add(oneGroup);
			i--;
		}

		return result;
	}

	public static void dropTable(Connection con) {
		JDBCUtil.dropTable(namespace.SydneyDBNameSpace.getIncomestatementDb(),
				con);
	}

	public static void createTable(Connection con) {
		JDBCUtil.excuteQuery(CREATETABLESQL, con);
	}

	public static String downLoadOneIncomeStatement(String code, Connection con) {
		String value = "";
		ArrayList<ArrayList<String>> resultList = new ArrayList<>();

		String[] viewList = { "Annual", "Quarterly" };
		for (String view : viewList) {
			Integer duration = 0;
			ArrayList<String> result = view.equals("Annual") ? UrlDao
					.getUrlBuffer("https://au.finance.yahoo.com/q/is?s=" + code
							+ ".AX&annual") : UrlDao
					.getUrlBuffer("https://au.finance.yahoo.com/q/is?s=" + code
							+ ".AX");
			Boolean ifHasResult = true;
			for (String ele : result) {
				if (ele.contains("The document has moved")
						|| ele.contains("There is no Income Statement data")) {
					ifHasResult = false;
				}
			}
			if (ifHasResult == true) {
				for (int j = 0; j < result.size(); j++) {
					if (result.get(j).contains("Period Ending")) {
						String[] hi = result.get(j).split("bold\">");
						for (int i = 1; i < hi.length; i++) {
							String period = hi[i].substring(0,
									hi[i].indexOf("</th>"));
							ArrayList<String> oneResultList = new ArrayList<>();
							oneResultList.add(code);
							oneResultList.add(view);
							oneResultList.add(changeIntoSqlDate(period));
							resultList.add(oneResultList);
							duration++;
						}
					}
				}
				if (duration > 0) {
					for (String item : itemList) {
						resultList = findValue(item, result, duration,
								resultList);
					}
				}
			}
		}
		resultList = substractSameRecord(resultList);
		value = changeToSqlValue(resultList);
		return value;
	}

	public static String changeIntoSqlDate(String input) {
		String result = "";
		String[] list = input.split("/");
		for (int i = list.length - 1; i >= 0; i--) {
			result += list[i] + "/";
		}
		return result.substring(0, result.length() - 1);
	}

	public static ArrayList<ArrayList<String>> findValue(String item,
			ArrayList<String> result, Integer duration,
			ArrayList<ArrayList<String>> resultList) {
		String mark = "right";
		for (int j = 0; j < result.size(); j++) {
			if (result.get(j).contains(item)) {
				String tempString = "";
				Integer flag = 0;
				while (flag < duration) {
					flag += charDeal.countMark(result.get(j), mark);
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
							output = "-" + charDeal.extractDigital(output)
									+ "000";
						} else {
							output = charDeal.extractDigital(output) + "000";
						}
					} else {
						output = "null";
					}
					resultList.get(resultList.size() - 1 - (duration - i)).add(
							output);
				}
				break;
			}
		}
		return resultList;
	}

	public static String changeToSqlValue(ArrayList<ArrayList<String>> input) {
		String result = "";
		for (ArrayList<String> oneResult : input) {
			result += changeToOneSqlValue(oneResult) + ",";
		}
		return result;
	}

	public static ArrayList<ArrayList<String>> substractSameRecord(
			ArrayList<ArrayList<String>> input) {
		ArrayList<Integer> numberList = new ArrayList<>();
		for (int i = 0; i < input.size() - 1; i++) {
			Boolean ifhasSame = false;
			for (int j = i + 1; j < input.size(); j++) {
				if (input.get(i).toString().equals(input.get(j))) {
					ifhasSame = true;
					break;
				}
			}
			if (ifhasSame == false) {
				numberList.add(i);
			}
		}
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		for (Integer element : numberList) {
			result.add(input.get(element));
		}
		return result;
	}

	public static String changeToOneSqlValue(ArrayList<String> input) {
		String result = "(";
		for (int i = 0; i < input.size(); i++) {
			if (i < 3) {
				result += "\'" + input.get(i) + "\',";
			} else {
				result += input.get(i) + ",";
			}
		}
		result = result.substring(0, result.length() - 1) + ")";
		return result;
	}

	public static void insertDataIntoDB(String value, Connection con) {
		String insertIntoDBSQL = "INSERT INTO "
				+ SydneyDBNameSpace.getIncomestatementDb()
				+ " (Local_Code,View,Ending_Period,Total_Revenue,Cost_of_Revenue,Gross_Profit,Research_Development,Selling_General_and_Administrative,"
				+ "Non_Recurring,Others,Total_Operating_Expenses,Operating_Income_or_Loss,`Total_Other_Income/Expenses_Net`,Earnings_Before_Interest_And_Taxes,"
				+ "Interest_Expense,Income_Before_Tax,Income_Tax_Expense,Minority_Interest,Net_Income_From_Continuing_Ops,DiscontinuedvOperations,Extraordinary_Items,"
				+ "Effect_Of_AccountingvChanges,Other_Items,Net_Income,PreferredvStock_And_Other_Adjustments,Net_Income_Applicable_To_Common_Shares) VALUES "
				+ value;
		// System.out.println(insertIntoDBSQL);
		JDBCUtil.excuteQuery(insertIntoDBSQL, con);
	}

	public static HashMap<String, String> getSection(Connection con) {
		HashMap<String, String> result = new HashMap<>();
		String getCodeSectionSql = "SELECT Local_Code, Section FROM "
				+ namespace.SydneyDBNameSpace.getListedcompaniesSydneyDb();
		ResultSet rs = null;
		try {
			rs = JDBCUtil.excuteQueryWithResult(getCodeSectionSql, con);
			while (rs.next()) {
				result.put(rs.getString("Local_Code"), rs.getString("Section"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
