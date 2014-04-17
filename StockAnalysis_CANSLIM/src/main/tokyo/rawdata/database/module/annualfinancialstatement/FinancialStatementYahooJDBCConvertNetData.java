package module.annualfinancialstatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FinancialStatementYahooJDBCConvertNetData {

	public FinancialStatementYahooJDBCConvertNetData() {
		// TODO Auto-generated constructor stub
	}

	public String FinancialStatementYahooJDBCConvertNetDataImpl(
			ArrayList<String> UrlInput, String itemString, Integer row)
			throws IOException, ParseException {

		String input = UrlInput.get(row);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月期");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");

		switch (itemString) {
		case "決算期":
			if (!(input.contains("---") || input.contains("‐年0月0日"))) {
				input = input.substring(18, input.length() - 5);
				Date date1 = sdf1.parse(input);
				input = sdf3.format(date1) + "01";
			}
			break;
		case "決算発表日":
			if (!(input.contains("---") || input.contains("‐年0月0日"))) {
				input = input.substring(18, input.length() - 5);
				Date date2 = sdf2.parse(input);
				input = sdf4.format(date2);
			} else
				input = null;
			break;
		case "決算月数":
			input = input.substring(18, input.length() - 5);
			input = input.substring(0, input.length() - 2);
			break;
		case "売上高":
		case "営業利益":
		case "経常利益":
		case "当期利益":
		case "総資産":
		case "自己資本":
		case "資本金":
		case "有利子負債":
			input = input.substring(18, input.length() - 5);
			if (!(input.equals("---") || input.contains("‥百万円"))) {
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
			break;
		case "EPS（一株当たり利益）":
		case "調整一株当たり利益":
		case "BPS（一株当たり純資産）":
		case "1株配当":
			if (!(input.contains("---") || input.contains("‐円"))) {
				input = input.substring(input.indexOf("\">") + 2,
						input.indexOf("円"));
				String inputSplit[] = input.split(",");
				input = "";
				for (int j = 0; j < inputSplit.length; j++) {
					input = input + inputSplit[j];
				}
			} else {
				input = null;
			}
			break;
		case "自己資本比率":
		case "含み損益":
		case "ROA（総資産利益率）":
		case "ROE（自己資本利益率）":
		case "総資産経常利益率":
			input = input.substring(18, input.length() - 5);
			if (!(input.equals("---") || input.contains("‐%") || input.contains("‥%") )) {
				input = input.substring(0, input.indexOf("%"));
				DecimalFormat df = new DecimalFormat("#.####");
				Double inputDouble = Double.parseDouble(df.format(Double
						.valueOf(input) / 100));
				input = inputDouble + "";
			} else {
				input = null;
			}
			break;
		case "発行済み株式総数":
			if (!input.contains("---")) {
				input = input.substring(18, input.length() - 5);
				input = input.substring(0, input.indexOf("千株"));
				String inputSplit[] = input.split(",");
				input = "";
				for (int j = 0; j < inputSplit.length; j++) {
					input = input + inputSplit[j];
				}
				input += "000";
			}
			break;
		case "配当区分":
		case "繰越損益":
			input = input.substring(18, input.length() - 5);
			if (input.equals("---")) {
				input = null;
			}
			break;
		}
		return input;
	}

	public String FinancialStatementYahooJDBCConvertNetDataImpl(
			String itemString, Integer fiscalYearNumber, BufferedReader fi)
			throws IOException, ParseException {

		String input = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月期");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");

		switch (itemString) {
		case "決算期":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
					input = input.substring(18, input.length() - 5);
					Date date = sdf1.parse(input);
					input = sdf3.format(date);
					break;
				}
			}
			break;
		case "決算発表日":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
					input = input.substring(18, input.length() - 5);
					Date date = sdf2.parse(input);
					input = sdf4.format(date);
					break;
				}
			}
			break;
		case "決算月数":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
					input = input.substring(18, input.length() - 5);
					input = input.substring(0, input.length() - 2);
					break;
				}
			}
			break;
		case "売上高":
		case "営業利益":
		case "経常利益":
		case "当期利益":
		case "総資産":
		case "自己資本":
		case "資本金":
		case "有利子負債":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
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
					break;
				}
			}
			break;
		case "EPS（一株当たり利益）":
		case "調整一株当たり利益":
		case "BPS（一株当たり純資産）":
		case "1株配当":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
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
					break;
				}
			}
			break;
		case "自己資本比率":
		case "含み損益":
		case "ROA（総資産利益率）":
		case "ROE（自己資本利益率）":
		case "総資産経常利益率":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
					input = input.substring(18, input.length() - 5);
					if (!input.equals("---")) {
						input = input.substring(0, input.indexOf("%"));
						DecimalFormat df = new DecimalFormat("#.####");
						Double inputDouble = Double.parseDouble(df
								.format(Double.valueOf(input) / 100));
						input = inputDouble + "";
					} else {
						input = null;
					}
					break;
				}
			}
			break;
		case "発行済み株式総数":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
					input = input.substring(18, input.length() - 5);
					input = input.substring(0, input.indexOf("千株"));
					String inputSplit[] = input.split(",");
					input = "";
					for (int j = 0; j < inputSplit.length; j++) {
						input = input + inputSplit[j];
					}
					input += "000";
					break;
				}
			}
			break;
		case "配当区分":
		case "繰越損益":
			for (int i = 0; i < 3; i++) {
				input = fi.readLine();
				if (i == fiscalYearNumber) {
					input = input.substring(18, input.length() - 5);
					if (input.equals("---")) {
						input = null;
					}
					break;
				}
			}
			break;
		}
		return input;
	}

	public static void inputIntoDataBase(String itemString,
			Integer fiscalYearNumber, BufferedReader fi) throws IOException,
			ParseException {

		String input = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月期");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
		Double inputDouble = 0.0;
		switch (itemString) {
		case "決算期":
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				Date date = sdf1.parse(input);
				input = sdf3.format(date);
				System.out.println(input);
			}
			break;
		case "決算発表日":
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				Date date = sdf2.parse(input);
				input = sdf4.format(date);
				System.out.println(input);
			}
			break;
		case "決算月数":
			for (int i = 0; i < fiscalYearNumber; i++) {
				input = fi.readLine();
				input = input.substring(18, input.length() - 5);
				input = input.substring(0, input.length() - 2);
				System.out.println(input);
			}
			break;
		case "売上高":
		case "営業利益":
		case "経常利益":
		case "当期利益":
		case "総資産":
		case "自己資本":
		case "資本金":
		case "有利子負債":
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
		case "EPS（一株当たり利益）":
		case "調整一株当たり利益":
		case "BPS（一株当たり純資産）":
		case "1株配当":
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
		case "自己資本比率":
		case "含み損益":
		case "ROA（総資産利益率）":
		case "ROE（自己資本利益率）":
		case "総資産経常利益率":
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
		case "発行済み株式総数":
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
		case "配当区分":
		case "繰越損益":
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
