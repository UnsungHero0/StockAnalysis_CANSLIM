package com.download.financialstatementquarterdownload.kabupro.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jdbc.util.JDBCUtil;
import com.namespace.DBNameSpace;

public class FinancialStatementQuarterDownLoadKabuproAnalysisCreateDB {

	public FinancialStatementQuarterDownLoadKabuproAnalysisCreateDB() {
		// TODO Auto-generated constructor stub
	}

	public static void createAnalysisDB(Connection con) {
		if (JDBCUtil
				.hasTable(DBNameSpace.getQuarterfinancialstatementDb(), con)) {
			ArrayList<FinancialStatementQuarterDownLoadKabuproAnalysisRecord> result = new ArrayList<>();

			System.out.println("start to fetch raw data from DB");
			result = FinancialStatementQuarterDownLoadKabuproAnalysisFetchRawData
					.fetchRawDataFromDB(con);
			System.out.println("over!\n");

			System.out.println("create analysis table");
			createAnalysisTable(con);
			System.out.println("over\n");

			System.out.println("insert data into DB");
			FinancialStatementQuarterDownLoadKabuproAnalysisInsertInertDB
					.insetIntoDB(result, con);
			System.out.println("over\n");
		}
	}

	public static void createAnalysisTable(Connection con) {

		System.out.println("drop tables");
		JDBCUtil.dropTable(
				DBNameSpace.getQuarterfinancialstatementanalysisDb(), con);
		System.out.println("drop tables OK\n");

		String maxY = getMaxFiscalMaxPeriod(con)[0];
		String maxP = getMaxFiscalMaxPeriod(con)[1];
		String minY = getMinFiscalMinPeriod(con)[0];
		String minP = getMinFiscalMinPeriod(con)[1];

		String fields = getFieldsString(maxY, maxP, minY, minP);

		String createTableSql = "CREATE TABLE IF NOT EXISTS "
				+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
				+ "( Country VARCHAR(50) NOT NULL Default 'Tokyo', "
				+ "Local_Code VARCHAR(20) NOT NULL, "
				+ "Name_English VARCHAR(100) NOT NULL, "
				+ "Analysis_Type VARCHAR(30) NOT NULL, " + fields
				+ "CONSTRAINT ID PRIMARY KEY (Local_Code, Analysis_Type));";
		System.out.println(createTableSql);
		try {
			con.prepareStatement(createTableSql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static String[] getMaxFiscalMaxPeriod(Connection con) {
		String[] result = new String[2];
		try {
			String sql = "SELECT Fiscal_Year, MAX(period) FROM "
					+ DBNameSpace.getQuarterfinancialstatementDb() + " WHERE "
					+ "Fiscal_Year = (SELECT MAX(Fiscal_Year) FROM "
					+ "QuarterFinancialStatementTokyo_test)";
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			rs.next();
			result[0] = rs.getString("Fiscal_Year").split("-")[0];
			result[1] = rs.getString("MAX(period)");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String[] getMinFiscalMinPeriod(Connection con) {
		String[] result = new String[2];
		try {
			String sql = "SELECT Fiscal_Year, MIN(period) FROM "
					+ DBNameSpace.getQuarterfinancialstatementDb() + " WHERE "
					+ "Fiscal_Year = (SELECT MIN(Fiscal_Year) FROM "
					+ "QuarterFinancialStatementTokyo_test)";
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			rs.next();
			result[0] = rs.getString("Fiscal_Year").split("-")[0];
			result[1] = rs.getString("MIN(period)");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getFieldsString(String maxY, String maxP, String minY,
			String minP) {
		String result = "";
		String[] item = { "Sales_", "Operating_Income_", "Net_Income_" };
		for (int i = 0; i < item.length; i++) {
			for (int j = Integer.valueOf(maxY); j >= Integer.valueOf(minY); j--) {
				if (j == Integer.valueOf(maxY)) {
					for (int k = Integer
							.valueOf(maxP.substring(maxP.length() - 1)); k >= 1; k--) {
						result += item[i] + String.valueOf(j) + "_Q" + k
								+ " Float , \n";
					}
				} else if (j == Integer.valueOf(minY)) {
					for (int k = 4; k >= Integer.valueOf(minP.substring(maxP
							.length() - 1)); k--) {
						result += item[i] + String.valueOf(j) + "_Q" + k
								+ " Float , \n";
					}
				} else {
					for (int k = 4; k >= 1; k--) {
						result += item[i] + String.valueOf(j) + "_Q" + k
								+ " Float , \n";
					}
				}
			}
		}
		return result;
	}

}
