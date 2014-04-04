package com.download.financialstatementquarterdownload.kabupro.jdbc;

import java.util.HashMap;
import java.util.Set;

import com.jdbc.util.ConvertToStringWithOutScientificNotation;

public class FinancialStatementQuarterDownLoadKabuproAnalysisRecord {
	
	private String Country = "Japan";
	private String Local_Code = "";
	private String Name_English = "";
	//key e.g. Sales_2015_Q4
	private HashMap<String, Float> Accumulation_Income = new HashMap<>();
	private HashMap<String, Float> Accumulation_Income_Increasement_Percent_Year = new HashMap<>();
	private HashMap<String, Float> Season_Income = new HashMap<>();
	private HashMap<String, Float> Season_Income_Increasement_Percent_Season = new HashMap<>();
	private HashMap<String, Float> Season_Income_Increasement_Percent_Year = new HashMap<>();

	public FinancialStatementQuarterDownLoadKabuproAnalysisRecord() {
		// TODO Auto-generated constructor stub
	}
	
	public FinancialStatementQuarterDownLoadKabuproAnalysisRecord(FinancialStatementQuarterDownLoadKabuproAnalysisRecord record) {
		setCountry(record.getCountry());
		setLocal_Code(record.getLocal_Code());
		setName_English(record.getName_English());
		setAccumulation_Income(record.getAccumulation_Income());
		setAccumulation_Income_Increasement_Percent_Year(record.getAccumulation_Income_Increasement_Percent_Year());
		setSeason_Income(record.getSeason_Income());
		setSeason_Income_Increasement_Percent_Season(record.getSeason_Income_Increasement_Percent_Season());
		setSeason_Income_Increasement_Percent_Year(record.getSeason_Income_Increasement_Percent_Year());
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

	public String getName_English() {
		return Name_English;
	}

	public void setName_English(String name_English) {
		Name_English = name_English;
	}

	public HashMap<String, Float> getAccumulation_Income() {
		return Accumulation_Income;
	}

	public void setAccumulation_Income(HashMap<String, Float> accumulation_Income) {
		Accumulation_Income = accumulation_Income;
	}

	public HashMap<String, Float> getAccumulation_Income_Increasement_Percent_Year() {
		return Accumulation_Income_Increasement_Percent_Year;
	}

	public void setAccumulation_Income_Increasement_Percent_Year(
			HashMap<String, Float> accumulation_Income_Increasement_Percent_Year) {
		Accumulation_Income_Increasement_Percent_Year = accumulation_Income_Increasement_Percent_Year;
	}

	public HashMap<String, Float> getSeason_Income() {
		return Season_Income;
	}

	public void setSeason_Income(HashMap<String, Float> season_Income) {
		Season_Income = season_Income;
	}

	public HashMap<String, Float> getSeason_Income_Increasement_Percent_Season() {
		return Season_Income_Increasement_Percent_Season;
	}

	public void setSeason_Income_Increasement_Percent_Season(
			HashMap<String, Float> season_Income_Increasement_Percent_Season) {
		Season_Income_Increasement_Percent_Season = season_Income_Increasement_Percent_Season;
	}

	public HashMap<String, Float> getSeason_Income_Increasement_Percent_Year() {
		return Season_Income_Increasement_Percent_Year;
	}

	public void setSeason_Income_Increasement_Percent_Year(
			HashMap<String, Float> season_Income_Increasement_Percent_Year) {
		Season_Income_Increasement_Percent_Year = season_Income_Increasement_Percent_Year;
	}
	
	public String[] getAccumulation_IncomeFieldValue(){
		String field = "(Country, Local_Code, Name_English, Analysis_Type, ";
		String value = "('Japan', '" + getLocal_Code() + "', '" + getName_English() + "', 'Accumulation_Income', " ;
		Set<String> keyList = getAccumulation_Income().keySet();
		for (String key : keyList) {
			field += key + ", ";
			value += ConvertToStringWithOutScientificNotation.covert(getAccumulation_Income().get(key)) + ", ";
		}
		String result[] = {field.substring(0,field.length()-2)+")",value.substring(0,value.length()-2)+")"}; 
		return result;
	}
	
	public String[] getAccumulation_Income_Increasement_Percent_YearFieldValue(){
		String field = "(Country, Local_Code, Name_English, Analysis_Type, ";
		String value = "('Japan', '" + getLocal_Code() + "', '" + getName_English() + "', 'Accumulation_Income_Increasement_Percent_Year', " ;
		Set<String> keyList = getAccumulation_Income_Increasement_Percent_Year().keySet();
		for (String key : keyList) {
			field += key + ", ";
			value += ConvertToStringWithOutScientificNotation.covert(getAccumulation_Income_Increasement_Percent_Year().get(key)) + ", ";
		}
		
		String result[] = {field.substring(0,field.length()-2)+")",value.substring(0,value.length()-2)+")"}; 
		return result;
	}
	
	public String[] getSeason_IncomeFieldValue(){
		String field = "(Country, Local_Code, Name_English, Analysis_Type, ";
		String value = "('Japan', '" + getLocal_Code() + "', '" + getName_English() + "', 'Season_Income', " ;
		Set<String> keyList = getSeason_Income().keySet();
		for (String key : keyList) {
			field += key + ", ";
			value += ConvertToStringWithOutScientificNotation.covert(getSeason_Income().get(key)) + ", ";
		}
		
		String result[] = {field.substring(0,field.length()-2)+")",value.substring(0,value.length()-2)+")"}; 
		return result;
	}
	
	public String[] getSeason_Income_Increasement_Percent_SeasonFieldValue(){
		String field = "(Country, Local_Code, Name_English, Analysis_Type, ";
		String value = "('Japan', '" + getLocal_Code() + "', '" + getName_English() + "', 'Season_Income_Increasement_Percent_Season', " ;
		Set<String> keyList = getSeason_Income_Increasement_Percent_Season().keySet();
		for (String key : keyList) {
			field += key + ", ";
			value += ConvertToStringWithOutScientificNotation.covert(getSeason_Income_Increasement_Percent_Season().get(key)) + ", ";
		}
		
		String result[] = {field.substring(0,field.length()-2)+")",value.substring(0,value.length()-2)+")"}; 
		return result;
	}
	
	public String[] getSeason_Income_Increasement_Percent_YearFieldValue(){
		String field = "(Country, Local_Code, Name_English, Analysis_Type, ";
		String value = "('Japan', '" + getLocal_Code() + "', '" + getName_English() + "', 'Season_Income_Increasement_Percent_Year', " ;
		Set<String> keyList = getSeason_Income_Increasement_Percent_Year().keySet();
		for (String key : keyList) {
			field += key + ", ";
			value += ConvertToStringWithOutScientificNotation.covert(getSeason_Income_Increasement_Percent_Year().get(key)) + ", ";
		}
		
		String result[] = {field.substring(0,field.length()-2)+")",value.substring(0,value.length()-2)+")"}; 
		return result;
	}
}
