package impl.download;

/**
 * 1. get code list
 * 2. get Name_English From DB
 * 3. get Info From URL to Record
 * 4. create Table in DB
 * 5. insert data into Table
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import module.quarterfinancialstatement.FinancialStatementQuarterDownLoadKabuproFetchUrl;
import module.quarterfinancialstatement.FinancialStatementQuarterDownLoadKabuproInsertDB;
import module.quarterfinancialstatement.FinancialStatementQuarterDownLoadKabuproRecord;
import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;
import jdbcdao.NameEnglishDao;

public class FinancialStatementQuarterDownLoadKabuproImpl {

	public FinancialStatementQuarterDownLoadKabuproImpl() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
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
