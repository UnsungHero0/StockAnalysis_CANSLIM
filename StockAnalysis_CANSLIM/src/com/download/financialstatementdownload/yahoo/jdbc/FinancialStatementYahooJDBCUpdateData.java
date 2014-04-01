package com.download.financialstatementdownload.yahoo.jdbc;

import java.awt.Checkbox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.download.historicaldatadownload.yahoo.downLoadJapaneseToEnglish;
import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;

/**
 * 1.check if there is record in independent , consolidate, interim by the
 * keywords: Local_Code, From, and Fiscal_year
 * 
 * @author Daytona
 * 
 */

public class FinancialStatementYahooJDBCUpdateData {

	private static final Exception ex = null;

	public FinancialStatementYahooJDBCUpdateData() {
		// TODO Auto-generated constructor stub
	}

	public void FinancialStatemetYahooJDBCUpdateDataImpl(String code)
			throws IOException, ParseException {
		// TODO
		// check and update
		String typeIndependent = "independent";
		String typeConsolidate = "consolidate";
		String typeInterim = "interim";
		FinancialStatemetYahooJDBCUpdateDataExistCheck(code, typeIndependent);
		FinancialStatemetYahooJDBCUpdateDataExistCheck(code, typeConsolidate);
		FinancialStatemetYahooJDBCUpdateDataExistCheck(code, typeInterim);
	}

	public void FinancialStatemetYahooJDBCUpdateDataExistCheck(String code,
			String type) throws IOException, ParseException {
		ArrayList<String> UrlInput = new FinancialStatementYahooJDBCUrlDao()
				.getFinancialStatmentPageBufferedReaderYahooToString(code, type);
		Integer fiscalYearNumber = findFiscalYear(UrlInput);

		String input = "";

		for (int i = 0; i < UrlInput.size(); i++) {
			input = UrlInput.get(i);
			if (input.equals("<tr bgcolor=\"#ffffff\">")) {
				input = UrlInput.get(++i);
				if (input.contains("決算期")) {
					Integer blankYearCount = 0;
					for (int j = 1; j <= fiscalYearNumber; j++) {
						input = UrlInput.get(++i);
						input = input.substring(18, input.length() - 5);
						SimpleDateFormat sdf1 = new SimpleDateFormat(
								"yyyy年MM月期");
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
						if (input.equals("---")) {
							blankYearCount++;
							j--;
						} else {
							Date date = sdf1.parse(input);
							input = sdf2.format(date);
							Boolean ifHas = checkNetCheckCodeWithDB(code, type,
									input + "01");
							if (ifHas.equals(false)) {
								FinancialStatementYahooJDBCRecord record = new FinancialStatementYahooJDBCRecord();
								record = new FinancialStatementYahooJDBCInsertData()
										.createFinancialStatementYahooJDBCRecord(
												UrlInput, j + blankYearCount);
								record.setCountry("Tokyo");
								record.setLocal_Code(code);
								record.setForm(type);
								record.setName_English(getEnglishName(code));
								new FinancialStatementYahooJDBCInsertData()
										.insertRecordIntoSqlDB(record);
							}
						}
					}
				}
			}
		}
	}

	public Boolean checkNetCheckCodeWithDB(String code, String type, String date) {
		// Check
		Boolean ifHas = false;
		String checkifHasRecord = "SELECT COUNT(Local_Code) count FROM `TokyoStockExchange_test`.`FinancialStatementTokyo_test` WHERE "
				+ "Local_Code = "
				+ code
				+ " AND Form = '"
				+ type
				+ "' AND Fiscal_year = " + date;
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ResultSet rs = con.prepareStatement(checkifHasRecord)
					.executeQuery();
			rs.next();
			if (rs.getInt("count") == 1) {
				ifHas = true;
			} else if (rs.getInt("count") > 1) {
				System.out.println(code + " " + type + " " + date
						+ " has multiple records");
				ifHas = true;
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
		return ifHas;
	}

	/*
	 * public void insertNewDataIntoDB(String code, String type, String date,
	 * BufferedReader fi, Integer fiscalYearNumber) throws IOException { // TODO
	 * String input = ""; while ((input = fi.readLine()) != null) { if
	 * (input.equals("<tr bgcolor=\"#ffffff\">")) { input = fi.readLine(); input
	 * = input.substring(22, input.length() - 5).trim(); //input =
	 * FinancialStatementYahooJDBCJapaneseToEnglish //
	 * .changeIntoEnglishNameFinancialStatement(input);
	 * 
	 * if (input.equals("決算期")) { for (int i = 0; i < fiscalYearNumber; i++) {
	 * input = fi.readLine(); input = input.substring(18, input.length() - 5);
	 * SimpleDateFormat sdf1 = new SimpleDateFormat( "yyyy年MM月期");
	 * SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM"); Date date =
	 * sdf1.parse(input); input = sdf2.format(date); Boolean ifHas =
	 * checkNetCheckCodeWithDB(code, type, input); if (ifHas.equals(false)) {
	 * insertNewDataIntoDB(code, type, input, fi, i); } } break; } } }
	 * fi.close(); }
	 */

	public void getNetCheckCode(BufferedReader fi, String code, String type)
			throws IOException, ParseException {
		// TODO

	}

	public static Integer findFiscalYear(BufferedReader fi) {
		Integer result = 0;
		String input = null;
		try {
			while ((input = fi.readLine()) != null) {
				if (input.contains("決算期")) {
					input = fi.readLine();
					if (!input.contains("---")) {
						result++;
					}
					input = fi.readLine();
					if (!input.contains("---")) {
						result++;
					}
					input = fi.readLine();
					if (!input.contains("---")) {
						result++;
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Integer findFiscalYear(ArrayList<String> UrlInput) {
		Integer result = 0;
		String input = null;
		for (int i = 0; i < UrlInput.size(); i++) {
			input = UrlInput.get(i);
			if (input.contains("決算期")) {
				i++;
				input = UrlInput.get(i);
				if (!(input.contains("---") || input.contains("tr"))) {
					result++;
				}
				i++;
				input = UrlInput.get(i);
				if (!(input.contains("---") || input.contains("tr"))) {
					result++;
				}
				i++;
				input = UrlInput.get(i);
				if (!(input.contains("---") || input.contains("tr"))) {
					result++;
				}
				break;
			}
		}
		return result;
	}

	public String getEnglishName(String code) {
		String name = null;
		Connection con = null;
		try {
			String selectEnglishNameSql = "SELECT Name_English FROM `TokyoStockExchange_test`.`Section_Tokyo` WHERE "
					+ "Local_code = " + code;
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ResultSet rs = con.prepareStatement(selectEnglishNameSql)
					.executeQuery();
			rs.next();
			name = rs.getString("Name_English");
			if(name.contains("'")) {
				String nameArray[] = name.split("'");
				name = "";
				for (int i = 0; i < nameArray.length; i ++) {
					if (i!=0) {
					name = name + "\\'" + nameArray[i];
					} else {
						name =  name + nameArray[i];
					}
				}
			}
			rs.close();
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
		return name;
	}
}
