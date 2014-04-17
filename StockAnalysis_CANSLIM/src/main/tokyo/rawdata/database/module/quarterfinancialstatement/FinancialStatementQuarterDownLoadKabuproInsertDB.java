package module.quarterfinancialstatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinancialStatementQuarterDownLoadKabuproInsertDB {
	
	private static final String dropTableSql = "DROP TABLE IF EXISTS `TokyoStockExchange_test`.`QuarterFinancialStatementTokyo_test`";
	
	private static final String createQuarterFinancialStatementSql = "CREATE TABLE IF NOT EXISTS `TokyoStockExchange_test`.`QuarterFinancialStatementTokyo_test` "
			+ "(Country VARCHAR(50) NOT NULL Default 'Japan', "
			+ "Local_Code VARCHAR(20) , "
			+ "Name_English VARCHAR(100) , "
			+ "Fiscal_Year Date, "
			+ "Period VARCHAR(20), "
			+ "Announcement_Date Date, "
			+ "Sales BIGINT, "
			+ "Operating_Income BIGINT, "
			+ "Net_Income BIGINT, "
			+ "CONSTRAINT ID PRIMARY KEY (Local_Code, Fiscal_Year, Period));";

	public FinancialStatementQuarterDownLoadKabuproInsertDB() {
		// TODO Auto-generated constructor stub
	}

	public static void createTalbeAndInsertDB(
			ArrayList<FinancialStatementQuarterDownLoadKabuproRecord> result,
			Connection con) {
		/*
		System.out.println("start drop the table");
		dropTable(con);
		System.out.println("finish drop the table");
		*/
		System.out.println("start create the table");
		createTable(con);
		System.out.println("finish create the table");
		
		System.out.println("start insert date to the table");
		insertIntoDB(result, con);
		System.out.println(""
				+ "finish insert date to the table");
	}

	public static void dropTable(Connection con) {
		try {
			con.prepareStatement(dropTableSql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createTable(Connection con) {
		try {
			con.prepareStatement(createQuarterFinancialStatementSql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertIntoDB(
			ArrayList<FinancialStatementQuarterDownLoadKabuproRecord> result,
			Connection con) {
		for (FinancialStatementQuarterDownLoadKabuproRecord record : result) {
			String insertSql = "INSERT INTO `TokyoStockExchange_test`.`QuarterFinancialStatementTokyo_test` "
					+ record.getFieldsForSqlDB()
					+ " VALUES "
					+ record.getValuesForSqlDB();
			try {
				con.prepareStatement(insertSql).execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
