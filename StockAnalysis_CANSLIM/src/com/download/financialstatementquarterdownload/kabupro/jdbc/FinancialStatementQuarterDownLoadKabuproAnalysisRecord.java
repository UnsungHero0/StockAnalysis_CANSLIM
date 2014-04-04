package com.download.financialstatementquarterdownload.kabupro.jdbc;

import java.util.HashMap;

public class FinancialStatementQuarterDownLoadKabuproAnalysisRecord {
	
	private String Country = "Japan";
	private String Local_Code = "";
	//key e.g. Sales_2015_Q4
	private HashMap<String, Float> Accumulation = new HashMap<>();
	private HashMap<String, Float> Accumulation_Percent_Last_Year = new HashMap<>();
	private HashMap<String, Float> Increasement_Last_Season = new HashMap<>();
	private HashMap<String, Float> Increasement_Percent_Last_Season = new HashMap<>();
	private HashMap<String, Float> Increasement_Last_Year = new HashMap<>();
	private HashMap<String, Float> Increasement_Percent_Last_Year = new HashMap<>();

	public FinancialStatementQuarterDownLoadKabuproAnalysisRecord() {
		// TODO Auto-generated constructor stub
	}

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

	public HashMap<String, Float> getAccumulation() {
		return Accumulation;
	}

	public void setAccumulation(HashMap<String, Float> accumulation) {
		Accumulation = accumulation;
	}

	public HashMap<String, Float> getAccumulation_Percent_Last_Year() {
		return Accumulation_Percent_Last_Year;
	}

	public void setAccumulation_Percent_Last_Year(
			HashMap<String, Float> accumulation_Percent_Last_Year) {
		Accumulation_Percent_Last_Year = accumulation_Percent_Last_Year;
	}

	public HashMap<String, Float> getIncreasement_Last_Season() {
		return Increasement_Last_Season;
	}

	public void setIncreasement_Last_Season(
			HashMap<String, Float> increasement_Last_Season) {
		Increasement_Last_Season = increasement_Last_Season;
	}

	public HashMap<String, Float> getIncreasement_Percent_Last_Season() {
		return Increasement_Percent_Last_Season;
	}

	public void setIncreasement_Percent_Last_Season(
			HashMap<String, Float> increasement_Percent_Last_Season) {
		Increasement_Percent_Last_Season = increasement_Percent_Last_Season;
	}

	public HashMap<String, Float> getIncreasement_Last_Year() {
		return Increasement_Last_Year;
	}

	public void setIncreasement_Last_Year(
			HashMap<String, Float> increasement_Last_Year) {
		Increasement_Last_Year = increasement_Last_Year;
	}

	public HashMap<String, Float> getIncreasement_Percent_Last_Year() {
		return Increasement_Percent_Last_Year;
	}

	public void setIncreasement_Percent_Last_Year(
			HashMap<String, Float> increasement_Percent_Last_Year) {
		Increasement_Percent_Last_Year = increasement_Percent_Last_Year;
	}
	
	

}
