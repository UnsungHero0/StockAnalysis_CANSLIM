package com.download.financialstatementdownload.yahoo.jdbc;

public class FinancialStatementYahooJDBCRecord {

	private String Country = "Japan";
	private String Name_English = null;
	private String Form = null;
	private String Fiscal_Year = null;
	private String Announcement_Date = null;
	private String Dividend_Classification = null;
	private String Local_Code = null;
	private Integer Closing_Month = null;
	private Long Sales = null;
	private Long Operating_Income = null;
	private Long Ordinary_Income = null;
	private Long Net_Income = null;
	private Long Outstanding_Shares = null;
	private Long Total_Assets = null;
	private Long Shareholders_Equity = null;
	private Long Capital = null;
	private Long Interest_Bearing_Debt = null;
	private Long PL_Brought_Forward = null;
	private Long Net_Unrealized_Gains = null;
	private Float EPS = null;
	private Float EPS_Adj = null;
	private Float Dividend_Per_Share = null;
	private Float BPS = null;
	private Float Capital_To_Asset_Ratio = null;
	private Float ROA = null;
	private Float ROE = null;
	private Float Ratio_Of_Ordinary_Income_To_Total_Assets = null;

	public FinancialStatementYahooJDBCRecord() {
		// TODO Auto-generated constructor stub
	}

	public void setValue(String name, String value) {
		switch (name) {
		case "Country":
			setCountry(value);
			break;
		case "Local_Code":
			setLocal_Code(value);
			;
			break;
		case "Name_English":
			setName_English(value);
			break;
		case "Form":
			setForm(value);
			break;
		case "Fiscal_Year":
			setFiscal_Year(value);
			break;
		case "Announcement_Date":
			setAnnouncement_Date(value);
			break;
		case "Dividend_Classification":
			setDividend_Classification(value);
			break;

		// Integer
		case "Closing_Month":
			setValue(name, Integer.valueOf(value));
			break;
		// Long
		case "Sales":
		case "Operating_Income":
		case "Ordinary_Income":
		case "Net_Income":
		case "Outstanding_Shares":
		case "Total_Assets":
		case "Shareholders_Equity":
		case "Capital":
		case "Interest_Bearing_Debt":
		case "PL_Brought_Forward":
		case "Net_Unrealized_Gains":
			if (!value.contains("---")){
			setValue(name, Long.valueOf(value));
			}
			break;
		// FLOAT
		case "EPS":
		case "EPS_Adj":
		case "Dividend_Per_Share":
		case "BPS":
		case "Capital_To_Asset_Ratio":
		case "ROA":
		case "ROE":
		case "Ratio_Of_Ordinary_Income_To_Total_Assets":
			if (!(value.equals(""))) {
				setValue(name, Float.valueOf(value));
			}
			break;
		}
	}

	public void setValue(String name, Long value) {
		switch (name) {
		case "Sales":
			setSales(value);
			break;
		case "Operating_Income":
			setOperating_Income(value);
			break;
		case "Ordinary_Income":
			setOrdinary_Income(value);
			break;
		case "Net_Income":
			setNet_Income(value);
			break;
		case "Outstanding_Shares":
			setOutstanding_Shares(value);
			break;
		case "Total_Assets":
			setTotal_Assets(value);
			break;
		case "Shareholders_Equity":
			setShareholders_Equity(value);
			break;
		case "Capital":
			setCapital(value);
			break;
		case "Interest_Bearing_Debt":
			setInterest_Bearing_Debt(value);
			break;
		case "PL_Brought_Forward":
			setPL_Brought_Forward(value);
			break;
		case "Net_Unrealized_Gains":
			setNet_Unrealized_Gains(value);
			break;
		}
	}

	public void setValue(String name, Integer value) {
		switch (name) {
		case "Closing_Month":
			setClosing_Month(value);
			break;
		}
	}

	public void setValue(String name, Float value) {
		switch (name) {
		case "EPS":
			setEPS(value);
			;
			break;
		case "EPS_Adj":
			setEPS_Adj(value);
			;
			break;
		case "Dividend_Per_Share":
			setDividend_Per_Share(value);
			break;
		case "BPS":
			setBPS(value);
			break;
		case "Capital_To_Asset_Ratio":
			setCapital_To_Asset_Ratio(value);
			break;
		case "ROA":
			setROA(value);
			break;
		case "ROE":
			setROE(value);
			break;
		case "Ratio_Of_Ordinary_Income_To_Total_Assets":
			setRatio_Of_Ordinary_Income_To_Total_Assets(value);
			break;
		}
	}

	public void printOutRecord() {
		System.out.println("Country " + getCountry() + ", ");
		System.out.println("Name_English " + getName_English() + ", ");
		System.out.println("Form " + getForm() + ", ");
		System.out.println("Fiscal_Year " + getFiscal_Year() + ", ");
		System.out
				.println("Announcement_Date " + getAnnouncement_Date() + ", ");
		System.out.println("Dividend_Classification "
				+ getDividend_Classification() + ", ");
		System.out.println("Local_Code  " + getLocal_Code() + ", ");
		System.out.println("Closing_Month " + getClosing_Month() + ", ");
		System.out.println("Sales " + getSales() + ", ");
		System.out.println("Operating_Income " + getOperating_Income() + ", ");
		System.out.println("Ordinary_Income  " + getOrdinary_Income() + ", ");
		System.out.println("Net_Income  " + getNet_Income() + ", ");
		System.out.println("Outstanding_Shares " + getOutstanding_Shares()
				+ ", ");
		System.out.println("Total_Assets " + getTotal_Assets() + ", ");
		System.out.println("Shareholders_Equity " + getShareholders_Equity()
				+ ", ");
		System.out.println("Capital " + getCapital() + ", ");
		System.out.println("Interest_Bearing_Debt "
				+ getInterest_Bearing_Debt() + ", ");
		System.out.println("PL_Brought_Forward " + getPL_Brought_Forward()
				+ ", ");
		System.out.println("Net_Unrealized_Gains " + getNet_Unrealized_Gains()
				+ ", ");
		System.out.println("EPS " + getEPS() + ", ");
		System.out.println("EPS_Adj " + getEPS_Adj() + ", ");
		System.out.println("Dividend_Per_Share " + getDividend_Per_Share()
				+ ", ");
		System.out.println("BPS " + getBPS() + ", ");
		System.out.println("Capital_To_Asset_Ratio "
				+ getCapital_To_Asset_Ratio() + ", ");
		System.out.println("ROA " + getROA() + ", ");
		System.out.println("ROE " + getROE() + ", ");
		System.out.println("Ratio_Of_Ordinary_Income_To_Total_Assets "
				+ getRatio_Of_Ordinary_Income_To_Total_Assets() + ", ");
	}

	public String getFieldsForSqlDB() {
		String result = "";
		result = "(Country, Name_English, Form, Fiscal_Year, "
				+ "Announcement_Date, Dividend_Classification, Local_Code, Closing_Month, "
				+ "Sales, Operating_Income, Ordinary_Income, Net_Income, Outstanding_Shares, "
				+ "Total_Assets, Shareholders_Equity, Capital, Interest_Bearing_Debt, PL_Brought_Forward, "
				+ "Net_Unrealized_Gains, EPS, EPS_Adj, Dividend_Per_Share, "
				+ "BPS, Capital_To_Asset_Ratio, ROA, ROE, Ratio_Of_Ordinary_Income_To_Total_Assets)";
		return result;
	}

	public String getValuesForSqlDB() {
		String result = "('" + getCountry() + "', '" + getName_English()
				+ "', '" + getForm() + "', " + getFiscal_Year() + ", "
				+ getAnnouncement_Date() + ", '" + getDividend_Classification()
				+ "', " + getLocal_Code() + ", " + getClosing_Month() + ", "
				+ getSales() + ", " + getOperating_Income() + ", "
				+ getOrdinary_Income() + ", " + getNet_Income() + ", "
				+ getOutstanding_Shares() + ", " + getTotal_Assets() + ", "
				+ getShareholders_Equity() + ", " + getCapital() + ", "
				+ getInterest_Bearing_Debt() + ", " + getPL_Brought_Forward()
				+ ", " + getNet_Unrealized_Gains() + ", " + getEPS() + ", "
				+ getEPS_Adj() + ", " + getDividend_Per_Share() + ", "
				+ getBPS() + ", " + getCapital_To_Asset_Ratio() + ", "
				+ getROA() + ", " + getROE() + ", "
				+ getRatio_Of_Ordinary_Income_To_Total_Assets() + ")";
		return result;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getName_English() {
		return Name_English;
	}

	public void setName_English(String name_English) {
		Name_English = name_English;
	}

	public String getForm() {
		return Form;
	}

	public void setForm(String form) {
		Form = form;
	}

	public String getFiscal_Year() {
		return Fiscal_Year;
	}

	public void setFiscal_Year(String fiscal_Year) {
		Fiscal_Year = fiscal_Year;
	}

	public String getAnnouncement_Date() {
		return Announcement_Date;
	}

	public void setAnnouncement_Date(String announcement_Date) {
		Announcement_Date = announcement_Date;
	}

	public String getDividend_Classification() {
		return Dividend_Classification;
	}

	public void setDividend_Classification(String dividend_Classification) {
		Dividend_Classification = dividend_Classification;
	}

	public String getLocal_Code() {
		return Local_Code;
	}

	public void setLocal_Code(String local_Code) {
		Local_Code = local_Code;
	}

	public Integer getClosing_Month() {
		return Closing_Month;
	}

	public void setClosing_Month(Integer closing_Month) {
		Closing_Month = closing_Month;
	}

	public Long getSales() {
		return Sales;
	}

	public void setSales(Long sales) {
		Sales = sales;
	}

	public Long getOperating_Income() {
		return Operating_Income;
	}

	public void setOperating_Income(Long operating_Income) {
		Operating_Income = operating_Income;
	}

	public Long getOrdinary_Income() {
		return Ordinary_Income;
	}

	public void setOrdinary_Income(Long ordinary_Income) {
		Ordinary_Income = ordinary_Income;
	}

	public Long getNet_Income() {
		return Net_Income;
	}

	public void setNet_Income(Long net_Income) {
		Net_Income = net_Income;
	}

	public Long getOutstanding_Shares() {
		return Outstanding_Shares;
	}

	public void setOutstanding_Shares(Long outstanding_Shares) {
		Outstanding_Shares = outstanding_Shares;
	}

	public Long getTotal_Assets() {
		return Total_Assets;
	}

	public void setTotal_Assets(Long total_Assets) {
		Total_Assets = total_Assets;
	}

	public Long getShareholders_Equity() {
		return Shareholders_Equity;
	}

	public void setShareholders_Equity(Long shareholders_Equity) {
		Shareholders_Equity = shareholders_Equity;
	}

	public Long getCapital() {
		return Capital;
	}

	public void setCapital(Long capital) {
		Capital = capital;
	}

	public Long getInterest_Bearing_Debt() {
		return Interest_Bearing_Debt;
	}

	public void setInterest_Bearing_Debt(Long interest_Bearing_Debt) {
		Interest_Bearing_Debt = interest_Bearing_Debt;
	}

	public Long getPL_Brought_Forward() {
		return PL_Brought_Forward;
	}

	public void setPL_Brought_Forward(Long pL_Brought_Forward) {
		PL_Brought_Forward = pL_Brought_Forward;
	}

	public Long getNet_Unrealized_Gains() {
		return Net_Unrealized_Gains;
	}

	public void setNet_Unrealized_Gains(Long net_Unrealized_Gains) {
		Net_Unrealized_Gains = net_Unrealized_Gains;
	}

	public Float getEPS() {
		return EPS;
	}

	public void setEPS(Float ePS) {
		EPS = ePS;
	}

	public Float getEPS_Adj() {
		return EPS_Adj;
	}

	public void setEPS_Adj(Float ePS_Adj) {
		EPS_Adj = ePS_Adj;
	}

	public Float getDividend_Per_Share() {
		return Dividend_Per_Share;
	}

	public void setDividend_Per_Share(Float dividend_Per_Share) {
		Dividend_Per_Share = dividend_Per_Share;
	}

	public Float getBPS() {
		return BPS;
	}

	public void setBPS(Float bPS) {
		BPS = bPS;
	}

	public Float getCapital_To_Asset_Ratio() {
		return Capital_To_Asset_Ratio;
	}

	public void setCapital_To_Asset_Ratio(Float capital_To_Asset_Ratio) {
		Capital_To_Asset_Ratio = capital_To_Asset_Ratio;
	}

	public Float getROA() {
		return ROA;
	}

	public void setROA(Float rOA) {
		ROA = rOA;
	}

	public Float getROE() {
		return ROE;
	}

	public void setROE(Float rOE) {
		ROE = rOE;
	}

	public Float getRatio_Of_Ordinary_Income_To_Total_Assets() {
		return Ratio_Of_Ordinary_Income_To_Total_Assets;
	}

	public void setRatio_Of_Ordinary_Income_To_Total_Assets(
			Float ratio_Of_Ordinary_Income_To_Total_Assets) {
		Ratio_Of_Ordinary_Income_To_Total_Assets = ratio_Of_Ordinary_Income_To_Total_Assets;
	}

}
