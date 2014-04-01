package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.download.historicaldatadownload.yahoo.downLoadJapaneseToEnglish;
import com.download.historicaldatadownload.yahoo.jdbc.dao.CodeListsDao;

public class urlTest {

	private final String createFinancialStatementTableSql = "CREATE TABLE IF NOT EXISTS `TokyoStockExchange_test`.`FinancialStatementTokyo`"
			+ "(`Country` VARCHAR(50) NOT NULL Default 'Japan',"
			+ "`Local_Code` INT,"
			+ "`Name_English` VARCHAR(100),"
			+ "`Fiscal_Year` Date,"
			+ "`Announcement_Date` Date,"
			+ "`Closing_Month` TINYINT,"
			+ "`Sales` BIGINT,"
			+ "`Operating_Income` BIGINT,"
			+ "`Ordinary_Income` BIGINT,"
			+ "`Net_Income` BIGINT,"
			+ "`EPS` DOUBLE,"
			+ "`EPS_Adj` Double,"
			+ "`Dividend_Per_Share` DOUBLE,"
			+ "`Dividend_Classification` VARCHAR(50),"
			+ "`BPS` DOUBLE,"
			+ "`Outstanding _Shares` BIGINT,"
			+ "`Total_Assets` BIGINT,"
			+ "`Shareholders_Equity` BIGINT,"
			+ "`Capital` BIGINT,"
			+ "`Interest_Bearing_Debt` BIGINT,"
			+ "`PL_Brought_Forward` BIGINT,"
			+ "`Capital_To_Asset_Ratio` DOUBLE,"
			+ "`Net_Unrealized_Gains` BIGINT,"
			+ "`ROA` DOUBLE,"
			+ "`ROE` DOUBLE,"
			+ "`Ratio_Of_Ordinary_Income_To_Total_Assets` DOUBLE);";
	
	private final String insertNewRecordIntoFinancialStatemetTableSqlString = "Insert INTO `TokyoStockExchange_test`.`FinancialStatementTokyo` "
			+ "(Local_Code, Name_English, Fiscal_Year, Announcement_Date, Closing_Month, Sales, Operating_Income, Ordinary_Income, "
			+ "Net_Income, EPS, EPS_Adj, Dividend_Per_Share, Dividend_Classification, BPS,　Outstanding_Shares, Total_Assets, Shareholders_Equity, "
			+ "Capital, Interest_Bearing_Debt, PL_Brought_Forward, Capital_To_Asset_Ratio, Net_Unrealized_Gains, ROA, ROE, Ratio_Of_Ordinary_Income_To_Total_Assets) "
			+ "Value (?)";

	public urlTest() {
	}

	public static void main(String args[]) throws IOException, ParseException {
		//CodeListsDao clDao = new CodeListsDao();
		//ArrayList<String> codeLists = clDao.getCodeLists();
		//for (String code : codeLists) {
		String code = "3501";
			String urlString = "http://profile.yahoo.co.jp/independent/" + code;
			URL url = new URL(urlString);
			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			BufferedReader fi = null;
			Boolean ifHasInfo = true;
			try {
				fi = new BufferedReader(new InputStreamReader(
						set.getInputStream()));
			} catch (IOException e) {
				ifHasInfo = false;
			}

			if (ifHasInfo == true) {
				System.out.println("code : " + code + "  " + ifHasInfo);
				String input = "";
				String charset = "";
				while ((input = fi.readLine()) != null) {
					if (input.contains("charset=")) {
						charset = input.substring(
								input.indexOf("charset=") + 8,
								input.length() - 2);
						break;
					}
				}
				fi = new BufferedReader(new InputStreamReader(
						set.getInputStream(), charset));
				Integer fiscalYearNumber = findFiscalYear(fi);
				System.out.println(fiscalYearNumber);
				fi = new BufferedReader(new InputStreamReader(url
						.openConnection().getInputStream(), charset));
				downLoadJapaneseToEnglish dljte = new downLoadJapaneseToEnglish();
				while ((input = fi.readLine()) != null) {
					if (input.equals("<tr bgcolor=\"#ffffff\">")) {
						input = fi.readLine();
						input = input.substring(22,
								input.length() - 5).trim();
						String englishiName = dljte.changeIntoEnglishNameFinancialStatement(input);
						System.out.println(input + " " + englishiName);
						Integer itemNumber = itemNumber(input);
						 for (int i = 0; i < fiscalYearNumber; i++) { 
							 input = fi.readLine();
							 System.out.println(input.substring(18, input.length() - 5));
							 }
					}
				}
				fi.close();
				set.disconnect();
			}
		//}
	}

	public static Boolean getIfHasInformation(BufferedReader fi)
			throws IOException {
		Boolean result = true;
		String input = "";
		while ((input = fi.readLine()) != null) {
			if (input.equals("企業情報ページが見つかりません。")) {
				result = false;
				break;
			}
		}
		return result;
	}

	public static Integer findFiscalYear(BufferedReader fi) throws IOException {
		Integer result = 0;
		String input = null;
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

		return result;
	}

	public static Integer itemNumber(String string) {
		Integer result = 0;
		switch (string) {
		case "決算期":
			result = 1;
			break;
		case "決算発表日":
			result = 2;
			break;
		case "決算月数":
			result = 3;
			break;
		case "売上高":
			result = 4;
			break;
		case "営業利益":
			result = 5;
			break;
		case "経常利益":
			result = 6;
			break;
		case "当期利益":
			result = 7;
			break;
		case "EPS（一株当たり利益）":
			result = 8;
			break;
		case "調整一株当たり利益":
			result = 9;
			break;
		case "BPS（一株当たり純資産）":
			result = 10;
			break;
		case "総資産":
			result = 11;
			break;
		case "自己資本":
			result = 12;
			break;
		case "資本金":
			result = 13;
			break;
		case "有利子負債":
			result = 14;
			break;
		case "自己資本比率":
			result = 15;
			break;
		case "含み損益":
			result = 16;
			break;
		case "ROA（総資産利益率）":
			result = 17;
			break;
		case "ROE（自己資本利益率）":
			result = 18;
			break;
		case "総資産経常利益率":
			result = 19;
			break;
		case "1株配当":
			result = 20;
			break;
		case "配当区分":
			result = 21;
			break;
		case "発行済み株式総数":
			result = 22;
			break;
		case "繰越損益":
			result = 23;
			break;

		}
		return result;
	}

	public static void inputIntoDataBase(Integer termNumber,
			Integer fiscalYearNumber, BufferedReader fi) throws IOException,
			ParseException {

		String input = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月期");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
		Double inputDouble = 0.0;
		switch (termNumber) {
		case 1:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				Date date = sdf1.parse(input);
				input = sdf3.format(date);
				System.out.println(input);
			}
			break;
		case 2:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				Date date = sdf2.parse(input);
				input = sdf4.format(date);
				System.out.println(input);
			}
			break;
		case 3:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				input = input.substring(0, input.length() - 2);
				System.out.println(input);
			}
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 11:
		case 12:
		case 13:
		case 14:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				if (!input.equals("---")) {
					input = input.substring(0, input.indexOf("百万円"));
					String inputSplit[] = input.split(",");
					input = "";
					for (int j = 0; j < inputSplit.length; j++) {
						input = input + inputSplit[j];
					}
					input += "000000";
				} else {
					input = null;
				}
				System.out.println(input);
			}
			break;
		case 8:
		case 9:
		case 10:
		case 20:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				if (!(input.equals("---") || input.equals("‐円"))) {
					input = input.substring(0, input.indexOf("円"));
					String inputSplit[] = input.split(",");
					input = "";
					for (int j = 0; j < inputSplit.length; j++) {
						input = input + inputSplit[j];
					}
				} else {
					input = null;
				}
				System.out.println(input);
			}
			break;
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				if (!input.equals("---")) {
					input = input.substring(0, input.indexOf("%"));
					DecimalFormat df = new DecimalFormat("#.####");
					inputDouble = Double.parseDouble(df.format(Double
							.valueOf(input) / 100));
				} else {
					input = null;
				}
				System.out.println(inputDouble);
			}
			break;
		case 22:
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				input = input.substring(0, input.indexOf("千株"));
				String inputSplit[] = input.split(",");
				input = "";
				for (int j = 0; j < inputSplit.length; j++) {
					input = input + inputSplit[j];
				}
				input += "000";
				System.out.println(input);
			}
			break;
		case 21:
		case 23:
			// TODO
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				if (input.equals("---")) {
					input = null;
				} else {
					System.out.println(input);
				}
			}
			break;

		}
	}
	
	
}
