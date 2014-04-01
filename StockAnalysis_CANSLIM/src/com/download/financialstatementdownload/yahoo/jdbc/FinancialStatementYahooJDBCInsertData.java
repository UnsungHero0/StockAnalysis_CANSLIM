package com.download.financialstatementdownload.yahoo.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;

public class FinancialStatementYahooJDBCInsertData {

	public FinancialStatementYahooJDBCInsertData() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertRecordIntoSqlDB(FinancialStatementYahooJDBCRecord record) {
		String insertSql = "INSERT INTO `TokyoStockExchange_test`.`FinancialStatementTokyo_test` "
				+ record.getFieldsForSqlDB()
				+ " VALUES "
				+ record.getValuesForSqlDB();
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			con.prepareStatement(insertSql).execute();
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

	public FinancialStatementYahooJDBCRecord createFinancialStatementYahooJDBCRecord(
			ArrayList<String> UrlInput, Integer fiscalYearNumber)
			throws IOException, ParseException {
		FinancialStatementYahooJDBCRecord record = new FinancialStatementYahooJDBCRecord();
		String input = "";
		FinancialStatementYahooJDBCJapaneseToEnglish changeEnglishName = new FinancialStatementYahooJDBCJapaneseToEnglish();
		FinancialStatementYahooJDBCConvertNetData convertNetData = new FinancialStatementYahooJDBCConvertNetData();
		for (int i = 0; i < UrlInput.size(); i++) {
			input = UrlInput.get(i);
			if (input.equals("<tr bgcolor=\"#ffffff\">")) {
				input = UrlInput.get(++i);
				input = input.substring(22, input.length() - 5)
						.trim();
				String itemName = changeEnglishName
						.changeIntoEnglishNameFinancialStatement(input);
				String value = convertNetData
						.FinancialStatementYahooJDBCConvertNetDataImpl(
								UrlInput, input, i + fiscalYearNumber);
				if (value != null) {
				record.setValue(itemName, value);
				}
			}
		}
		return record;
	}

}
