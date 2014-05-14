package impl.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import jdbcdao.CodeListsDao;
import jdbcdao.NameEnglishDao;
import module.quarterfinancialstatement.FinancialStatementQuarterDownLoadKabuproFetchUrl;
import module.quarterfinancialstatement.FinancialStatementQuarterDownLoadKabuproInsertDB;
import module.quarterfinancialstatement.FinancialStatementQuarterDownLoadKabuproRecord;
import datasource.DataSourceUtil;

/**
 * 1.getCodeList, and getUrl info
 * 2. get the first record, sort as annoucement_date the identification word is code+fiscalyear+period (1) 
 * or code+fiscalyear_Prediction+period (2)
 * 3. if no same identification (1), insert into DB, otherwise, stop to the next code
 * 4. if has same identification (2), compare the announcement date, 
 * delete the old duplicated one, and insert new one
 * @author Daytona
 *
 */
public class FinancialStatementQuarterDownLoadKabuproUpdate {

	public FinancialStatementQuarterDownLoadKabuproUpdate() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		run();
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
	}

	public static void run() {
		HashMap<String, String> exception = new HashMap<>();
		System.out.println("start get code list!");
		ArrayList<String> codeList = new CodeListsDao()
				.getCodeListsFromFinancialStatement();
		System.out.println("finish get code list!");
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			System.out.println("start get code name!");
			HashMap<String, String> nameEnglish = NameEnglishDao
					.getNameEnglishDao(con);
			System.out.println("finish get code name!");
			/*
			System.out.println("start drop the table");
			FinancialStatementQuarterDownLoadKabuproInsertDB.dropTable(con);
			System.out.println("finish drop the table");
			System.out.println("start create the table");
			FinancialStatementQuarterDownLoadKabuproInsertDB.createTable(con);
			System.out.println("finish create the table");
			*/
			for (String code : codeList) {
				try {
					System.out.println("download " + code + ", "
							+ (codeList.size() - codeList.indexOf(code))
							+ " to go!!");
					FinancialStatementQuarterDownLoadKabuproRecord record = new FinancialStatementQuarterDownLoadKabuproRecord();
					record.setLocal_Code(code);
					record.setName_English(nameEnglish.get(code));
					ArrayList<FinancialStatementQuarterDownLoadKabuproRecord> resultTemp = FinancialStatementQuarterDownLoadKabuproFetchUrl
							.setRecordFromURL(record);
					System.out.println("start insert " + code + " date to the table");
					FinancialStatementQuarterDownLoadKabuproInsertDB.insertIntoDB(
							resultTemp, con);
					System.out.println("" + "finish insert " + code + " date to the table");
				} catch (Exception e) {
					e.printStackTrace();
					exception.put(code, e.getMessage());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		Set<String> keySet = exception.keySet();
		for (String string : keySet) {
			System.out.println(string + "   " + exception.get(string));
		}
	}
}

