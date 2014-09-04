package module.listedcompanydownload;

/**
 * download listed companies in NASDAQ, NYSE, AMEX market
 */

import java.sql.Connection;
import java.util.ArrayList;

import commontool.JDBCUtil;
import namespace.NewYorkDBNameSpace;
import dao.DateDao;
import dao.UrlDao;

public class ListedCompanyDownloadNewYorkImpl {

	private final static String CREATETABLESQL = "CREATE TABLE IF NOT EXISTS "
			+ NewYorkDBNameSpace.getListedcompaniesNewYorkDb() + " ("
			+ "`Effective_Date` DATE NOT NULL,"
			+ "`Country` VARCHAR(50) NOT NULL,"
			+ "`Section` VARCHAR(10) NOT NULL,"
			+ "`Local_Code` VARCHAR(10) NOT NULL,"
			+ "`Name_English` VARCHAR(100) NOT NULL,"
			+ "`Market_Cap` BIGINT ," + "`ADR_TSO` VARCHAR(10) ,"
			+ "`IPO_Year` VARCHAR(4) ," + "`Sector` VARCHAR(30) ,"
			+ "`Department` VARCHAR(100) NOT NULL,"
			+ "PRIMARY KEY (`Local_Code`, `Name_English`))";

	public static void downloadListedCompanyList(Connection con) {

		// 1.get the content from the web site including three markets
		ArrayList<ArrayList<String>> content = getListContentFromUrl();

		// 2.drop the table in DB if necessary
		JDBCUtil.dropTable(NewYorkDBNameSpace.getListedcompaniesNewYorkDb(),
				con);

		// 3.create the table inDB
		JDBCUtil.excuteQuery(CREATETABLESQL, con);

		// 4.insert data into DB
		insertDataIntoDB(content, con);
	}

	public static ArrayList<ArrayList<String>> getListContentFromUrl() {
		ArrayList<String> urlList = new ArrayList<>();
		// NASDAQ
		urlList.add("http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=NASDAQ&render=download");
		// NYSE
		urlList.add("http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=NYSE&render=download");
		// AMEX
		urlList.add("http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=AMEX&render=download");

		ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
		for (int i = 0; i < urlList.size(); i++) {
			ArrayList<String> result = UrlDao.getUrlBuffer(urlList.get(i));

			for (int j = 1; j < result.size(); j++) {
				ArrayList<String> oneResult = new ArrayList<>();
				if (i == 0) {
					oneResult.add("NASDAQ");
				} else if (i == 1) {
					oneResult.add("NYSE");
				} else {
					oneResult.add("AMEX");
				}
				for (String elee : result.get(j).split("\",")) {
					oneResult.add(elee.substring(1));
				}
				finalResult.add(oneResult);
			}
		}
		return finalResult;
	}

	public static void insertDataIntoDB(ArrayList<ArrayList<String>> content,
			Connection con) {
		String insertDataIntoDB = "INSERT INTO "
				+ NewYorkDBNameSpace.getListedcompaniesNewYorkDb()
				+ " (Effective_Date, Country,Section,Local_Code,Name_English,Market_Cap,ADR_TSO,IPO_Year,Sector,Department) VALUES "
				+ changeToValues(content);
		JDBCUtil.excuteQuery(insertDataIntoDB, con);
	}

	public static String changeToValues(ArrayList<ArrayList<String>> content) {
		String finalResult = "";
		for (ArrayList<String> element : content) {
			finalResult += changeToValuesString(element);
		}

		return finalResult.substring(0, finalResult.length() - 1);
	}

	public static String changeToValuesString(ArrayList<String> content) {
		String result = "('" + DateDao.dateTodayInMysqlForm() + "','NewYork','";
		for (int i = 0; i <= 8; i++) {
			if (i !=3) {
			result += dealChar(content.get(i)) + "','";
			}
		}
		result = result.substring(0, result.length() - 2) + "),";
		return result;
	}

	public static String dealChar(String input) {
		String result = "";
		for (Character ele : input.toCharArray()) {
			if (ele.toString().equals("'")) {
				result += "\\'";
			} else {
				result += ele;
			}
		}
		return result;
	}

}
