package module.annualfinancialstatement;

import impl.download.FinancialStatementYahooJDBCImpl;

import java.sql.Connection;
import java.sql.SQLException;

import namespace.DBNameSpace;


public class FinancialStatementYahooJDBCCreateTable {

	private final String createFinancialStatementSql = "CREATE TABLE IF NOT EXISTS " + DBNameSpace.getFinancailstatementDb() + " "
			+ "(Country VARCHAR(50) NOT NULL Default 'Japan', "
			+ "Local_Code VARCHAR(20) NOT NULL Default NULL, "
			+ "Name_English VARCHAR(100), "
			+ "Form VARCHAR(20) NOT NULL Default NULL, "
			+ "Fiscal_Year Date NOT NULL Default NULL, "
			+ "Announcement_Date Date, "
			+ "Closing_Month TINYINT, "
			+ "Sales BIGINT, "
			+ "Operating_Income BIGINT, "
			+ "Ordinary_Income BIGINT, "
			+ "Net_Income BIGINT, "
			+ "EPS FLOAT, "
			+ "EPS_Adj FLOAT, "
			+ "Dividend_Per_Share FLOAT, "
			+ "Dividend_Classification VARCHAR(50), "
			+ "BPS FLOAT, "
			+ "Outstanding_Shares BIGINT, "
			+ "Total_Assets BIGINT, "
			+ "Shareholders_Equity BIGINT, "
			+ "Capital BIGINT, "
			+ "Interest_Bearing_Debt BIGINT, "
			+ "PL_Brought_Forward BIGINT, "
			+ "Capital_To_Asset_Ratio FLOAT, "
			+ "Net_Unrealized_Gains DOUBLE, "
			+ "ROA FLOAT, "
			+ "ROE FLOAT, "
			+ "Ratio_Of_Ordinary_Income_To_Total_Assets FLOAT, "
			+ "PRIMARY KEY (Local_Code, Form, Fiscal_Year))";

	private String dropTable = "DROP TABLE " + namespace.DBNameSpace.getFinancailstatementDb();

	public FinancialStatementYahooJDBCCreateTable() {
		// TODO Auto-generated constructor stub
	}

	public void FinancialStatementYahooJDBCCreateTableImpl() {
		System.out.println("Start creating financial statement table...");
		Connection con = null;
		try {
			con = new FinancialStatementYahooJDBCImpl().getDataSource()
					.getConnection();
			con.prepareStatement(this.createFinancialStatementSql).execute();
			System.out.println("Financial statement table created!");
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

	}

	public void DropFinancialStatementTable() {

		System.out.println("Start dropping financial statement table...");
		Connection con = null;
		try {
			con = new FinancialStatementYahooJDBCImpl().getDataSource()
					.getConnection();
			con.prepareStatement(this.dropTable).execute();
			System.out.println("Financial statement table dropped!");

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
	}

}
