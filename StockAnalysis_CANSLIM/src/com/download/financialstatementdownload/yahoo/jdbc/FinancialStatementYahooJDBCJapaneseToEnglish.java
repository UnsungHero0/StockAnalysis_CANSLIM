package com.download.financialstatementdownload.yahoo.jdbc;

public class FinancialStatementYahooJDBCJapaneseToEnglish {

	public FinancialStatementYahooJDBCJapaneseToEnglish() {
		// TODO Auto-generated constructor stub
	}
	
	public String changeIntoEnglishNameFinancialStatement(String string) {
		String result = "";
		switch (string) {
		case "決算期":
			result = "Fiscal_Year";
			break;
		case "決算発表日":
			result = "Announcement_Date";
			break;
		case "決算月数":
			result = "Closing_Month";
			break;
		case "売上高":
			result = "Sales";
			break;
		case "営業利益":
			result = "Operating_Income";
			break;
		case "経常利益":
			result = "Ordinary_Income";
			break;
		case "当期利益":
			result = "Net_Income";
			break;
		case "EPS（一株当たり利益）":
			result = "EPS";
			break;
		case "調整一株当たり利益":
			result = "EPS_Adj";
			break;
		case "BPS（一株当たり純資産）":
			result = "BPS";
			break;
		case "総資産":
			result = "Total_Assets";
			break;
		case "自己資本":
			result = "Shareholders_Equity";
			break;
		case "資本金":
			result = "Capital";
			break;
		case "有利子負債":
			result = "Interest_Bearing_Debt";
			break;
		case "自己資本比率":
			result = "Capital_To_Asset_Ratio";
			break;
		case "含み損益":
			result = "Net_Unrealized_Gains";
			break;
		case "ROA（総資産利益率）":
			result = "ROA";
			break;
		case "ROE（自己資本利益率）":
			result = "ROE";
			break;
		case "総資産経常利益率":
			result = "Ratio_Of_Ordinary_Income_To_Total_Assets";
			break;
		case "1株配当":
			result = "Dividend_Per_Share";
			break;
		case "配当区分":
			result = "Dividend_Classification";
			break;
		case "発行済み株式総数":
			result = "Outstanding_Shares";
			break;
		case "繰越損益":
			result = "PL_Brought_Forward";
			break;
		}
		return result;
	}

}
