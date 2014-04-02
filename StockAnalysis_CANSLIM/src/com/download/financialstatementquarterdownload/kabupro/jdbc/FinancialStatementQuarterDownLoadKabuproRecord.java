package com.download.financialstatementquarterdownload.kabupro.jdbc;

public class FinancialStatementQuarterDownLoadKabuproRecord {

	private String Country = "Japan";
	private String Local_Code = null;
	private String Name_English = null;
	private String Fiscal_Year = null;
	private String Period = null;
	private String Announcement_Date = null;
	private Long Sales = null;
	private Long Operating_Income = null;
	private Long Net_Income = null;

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getLocal_Code() {
		return Local_Code;
	}

	public void setLocal_Code(String local_Code) {
		Local_Code = local_Code;
	}

	public String getName_English() {
		return Name_English;
	}

	public void setName_English(String name_English) {
		Name_English = name_English;
	}

	public String getFiscal_Year() {
		return Fiscal_Year;
	}

	public void setFiscal_Year(String fiscal_Year) {
		Fiscal_Year = fiscal_Year;
	}

	public String getPeriod() {
		return Period;
	}

	public void setPeriod(String period) {
		Period = period;
	}

	public String getAnnouncement_Date() {
		return Announcement_Date;
	}

	public void setAnnouncement_Date(String announcement_Date) {
		Announcement_Date = announcement_Date;
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

	public Long getNet_Income() {
		return Net_Income;
	}

	public void setNet_Income(Long net_Income) {
		Net_Income = net_Income;
	}

	public FinancialStatementQuarterDownLoadKabuproRecord() {
		// TODO Auto-generated constructor stub
	}

	public String getValuesForSqlDB() {
		String string = null;
		string = "('" + getCountry() + "', " + getLocal_Code() + ", '"
				+ getName_English() + "', " + getFiscal_Year() + ", '"
				+ getPeriod() + "', " + getAnnouncement_Date() + ", "
				+ getSales() + ", " + getOperating_Income() + ", "
				+ getNet_Income() + ")";
		return string;
	}

	public String getFieldsForSqlDB() {
		String string = null;
		string = "(Country, Local_Code, Name_English, Fiscal_Year, Period, "
				+ "Announcement_Date, Sales, Operating_Income, Net_Income)";
		return string;
	}

}
