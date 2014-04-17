package module.historicalquotes;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datasource.DataSourceUtil;

public class CreateQuotesTableFromCsv {

	public CreateQuotesTableFromCsv() {
	}

	public static void main(String args[]) {
		File fileList[] = (new File(
				"/Users/Daytona/Documents/workspace/StockAnalyzeJapan/Result"))
				.listFiles();
		Integer size = fileList.length;
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (File file : fileList) {
				if (file.getName().contains("csv")) {
					// File file = new
					// File("/Users/Daytona/Documents/workspace/StockAnalyzeJapan/Result/5935T.csv");
					createQuoteTable(con, file);
					System.out.println(file.getName() + " is created! "
							+ size--);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		/*
		 * File file = new File(
		 * "/Users/Daytona/Documents/workspace/StockAnalyzeJapan/Result/1301T.csv"
		 * ); Connection con = null; try { con =
		 * DataSourceUtil.getTokyoDataSourceRoot().getConnection();
		 * createQuoteTable(con,file); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}

	public static void createQuoteTable(Connection con, File file) {
		// TODO
		try {
			String tableName = file.getName().substring(0,
					file.getName().indexOf("T"))
					+ "_HistoricalQuotes_Tokyo";
			Integer code = Integer.parseInt(file.getName().substring(0,
					file.getName().indexOf("T")));
			String name = "";
			String sqlFindName = "SELECT Name_English FROM Section_Tokyo WHERE Local_Code = "
					+ file.getName().substring(0, file.getName().indexOf("T"));
			ResultSet rs = con.prepareStatement(sqlFindName).executeQuery();
			rs.next();
			name = rs.getString("Name_English");
			String sqlCreateTable = "CREATE TABLE IF NOT EXISTS `TokyoStockExchange_test`.`"
					+ tableName
					+ "` ("
					+ "`Country` VARCHAR(50) NOT NULL Default ?,"
					+ "`Local_Code` INT NOT NULL Default ?,"
					+ "`Name_English` VARCHAR(100) NOT NULL Default ?,"
					+ "`Date` DATE NOT NULL,"
					+ "`Open` INT NOT NULL,"
					+ "`High` INT NOT NULL,"
					+ "`Low` INT NOT NULL,"
					+ "`Close` INT NOT NULL,"
					+ "`Volume` INT NOT NULL,"
					+ "`AdjClose` INT NOT NULL,"
					+ "PRIMARY KEY (`Date`))"
					+ "ENGINE = InnoDB;";

			PreparedStatement stmt = con.prepareStatement(sqlCreateTable);
			stmt.setString(1, "Japan");
			stmt.setInt(2, code);
			stmt.setString(3, name);
			stmt.execute();
			// String sqlDelete = "truncate `TokyoStockExchange`.`" + tableName
			// +"`";
			// con.prepareCall(sqlDelete).execute();
			String sqlInsertQuotesIntoTable = "LOAD DATA LOCAL INFILE ? INTO TABLE "
					+ tableName
					+ " "
					+ "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' "
					+ "LINES TERMINATED BY '\n' STARTING BY '' "
					+ "IGNORE 1 LINES "
					+ "(Date,Open,High,Low,Close,Volume,AdjClose)";
			stmt = con.prepareStatement(sqlInsertQuotesIntoTable);
			stmt.setString(1, file.getAbsolutePath());
			stmt.execute();
			// printResultSet(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void printResultSet(ResultSet rs) {
		try {
			while (rs.next()) {
				System.out.println(rs.getWarnings());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
