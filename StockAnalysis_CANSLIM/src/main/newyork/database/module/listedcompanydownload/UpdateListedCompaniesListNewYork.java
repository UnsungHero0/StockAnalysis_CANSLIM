package module.listedcompanydownload;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import namespace.NewYorkDBNameSpace;
import tool.consolePrint;
import commontool.JDBCUtil;
import dao.DBNewYorkDao;
import dao.DateDao;
import dao.UrlDao;

public class UpdateListedCompaniesListNewYork {

	public static void updateListedCompanyList(Connection con) {

		// 1.get latest list from the web site
		ArrayList<ArrayList<String>> listContent = DownLoadListedCompanyNewYork
				.getListContentFromUrl();
		HashMap<String, ArrayList<String>> latest_listed_company = turnListContentIntoHashMap(listContent);

		// 2. get old list from DB
		ArrayList<String> old_code_list = getOldListedCompanyFromDB(con);

		// 3. find new listed company
		HashMap<String, ArrayList<String>> new_code_company = getNewListedCompany(
				latest_listed_company, old_code_list);

		// 4.update listed company table in DB
		insertDataIntoDB(new_code_company, con);

	}

	private static HashMap<String, ArrayList<String>> turnListContentIntoHashMap(
			ArrayList<ArrayList<String>> listContent) {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		for (ArrayList<String> oneRecord : listContent) {
			result.put(oneRecord.get(1), oneRecord);
		}
		return result;
	}

	private static ArrayList<String> getOldListedCompanyFromDB(Connection con) {
		return DBNewYorkDao.getCodeListFromDB(con);
	}

	private static HashMap<String, ArrayList<String>> getNewListedCompany(
			HashMap<String, ArrayList<String>> latest_listed_company,
			ArrayList<String> old_code_list) {
		ArrayList<String> latest_code_list = new ArrayList<>(
				latest_listed_company.keySet());
		for (int i = 0; i < latest_code_list.size(); i++) {
			if (old_code_list.contains(latest_code_list.get(i))) {
				latest_listed_company.remove(latest_code_list.get(i));
			}
		}

		ArrayList<String> new_code_list = new ArrayList<>(
				latest_listed_company.keySet());
		// sometime the company is listed in file, but it is just finished IPO,
		// not start exchange in market
		for (String code : new_code_list) {
			if (checkIfStartExchange(code) == false) {
				consolePrint
						.println(latest_listed_company.get(code).get(0)
								+ ", "
								+ latest_listed_company.get(code).get(1)
								+ " , has finished IPO, but not ready for exchanging yet.");
				latest_listed_company.remove(code);
			}
		}

		if (latest_listed_company.keySet().size() == 0) {
			consolePrint.println("No new listed company!");
		} else {
			consolePrint.print("New listed company code : ");
			for (String code : latest_listed_company.keySet()) {
				consolePrint.print(code + " , ");
			}
			consolePrint.println("");
		}

		return latest_listed_company;
	}

	private static Boolean checkIfStartExchange(String code) {
		String csvUrl = "http://real-chart.finance.yahoo.com/table.csv?s="
				+ code + "&a=00&b=1&c=1950&" + "d="
				+ (Integer.valueOf(DateDao.month) - 1) + "&" + "e="
				+ (DateDao.day) + "&" + "f=" + (DateDao.year) + "&"
				+ "g=d&ignore=.csv";
		ArrayList<String> urlResult = UrlDao.getUrlBuffer(csvUrl);
		if (urlResult.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private static void insertDataIntoDB(
			HashMap<String, ArrayList<String>> new_code_company, Connection con) {
		String insertDataIntoDB = "INSERT INTO "
				+ NewYorkDBNameSpace.getListedcompaniesNewYorkDb()
				+ " (Effective_Date, Country,Section,Local_Code,Name_English,Market_Cap,ADR_TSO,IPO_Year,Sector,Department) VALUES "
				+ changeToValues(new_code_company);
		System.out.println(insertDataIntoDB);
		JDBCUtil.excuteQuery(insertDataIntoDB, con);

		// 2. update effective data in table
		String updateEffectiveDateQuery = "update "
				+ NewYorkDBNameSpace.getListedcompaniesNewYorkDb()
				+ " set Effective_date = '" + DateDao.dateTodayInMysqlForm()
				+ "'";
		JDBCUtil.excuteQuery(updateEffectiveDateQuery, con);
		consolePrint.println("Listed company table is updated to "
				+ DateDao.dateTodayInMysqlForm());
	}
	
	public static String changeToValues(HashMap<String, ArrayList<String>> content) {
		String finalResult = "";
		for (String code : content.keySet()) {
			finalResult += changeToValuesString(content.get(code));
		}
		return finalResult.substring(0, finalResult.length() - 1);
	}
	
	public static String changeToValuesString(ArrayList<String> content) {
		String result = "(\'" + DateDao.dateTodayInMysqlForm() + "\',\'NewYork\',\'";
		for (int i = 0; i <= 8; i++) {
			if (i == 1) {
				result += dealChar(content.get(i).trim()) + "\',\'";
			}
			if (i != 3 && i != 1) {
				result += dealChar(content.get(i)) + "\',\'";
			}
		}
		result = result.substring(0, result.length() - 2) + "),";
		return result;
	}

	private static String dealChar(String input) {
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
