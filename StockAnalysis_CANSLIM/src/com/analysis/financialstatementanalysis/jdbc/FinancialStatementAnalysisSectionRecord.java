package com.analysis.financialstatementanalysis.jdbc;

import java.util.ArrayList;

public class FinancialStatementAnalysisSectionRecord {

	public FinancialStatementAnalysisSectionRecord() {
		// TODO Auto-generated constructor stub
	}
	
	private String Local_Code = "";
	private ArrayList<Float> ePSArray = new ArrayList<>();
	private ArrayList<Float> ePSGrowthRateArray = new ArrayList<>();
	private Float ePSAverageGrowthRate = (float) 0.0;
	private ArrayList<Float> bPSArray = new ArrayList<>();
	private ArrayList<Float> bPSGrowthRateArray = new ArrayList<>();
	private Float bPSAverageGrowthRate = (float) 0.0;
	private String sector_Name = "";
	private Float rSI = (float) 0.0;
	
	public String getLocal_Code() {
		return Local_Code;
	}
	public void setLocal_Code(String local_Code) {
		Local_Code = local_Code;
	}
	public ArrayList<Float> getePSArray() {
		return ePSArray;
	}
	public void setePSArray(ArrayList<Float> ePSArray) {
		this.ePSArray = ePSArray;
	}
	public ArrayList<Float> getePSGrowthRateArray() {
		return ePSGrowthRateArray;
	}
	public void setePSGrowthRateArray(ArrayList<Float> ePSGrowthRateArray) {
		this.ePSGrowthRateArray = ePSGrowthRateArray;
	}
	public Float getePSAverageGrowthRate() {
		return ePSAverageGrowthRate;
	}
	public void setePSAverageGrowthRate(Float ePSAverageGrowthRate) {
		this.ePSAverageGrowthRate = ePSAverageGrowthRate;
	}
	public ArrayList<Float> getbPSArray() {
		return bPSArray;
	}
	public void setbPSArray(ArrayList<Float> bPSArray) {
		this.bPSArray = bPSArray;
	}
	public ArrayList<Float> getbPSGrowthRateArray() {
		return bPSGrowthRateArray;
	}
	public void setbPSGrowthRateArray(ArrayList<Float> bPSGrowthRateArray) {
		this.bPSGrowthRateArray = bPSGrowthRateArray;
	}
	public Float getbPSAverageGrowthRate() {
		return bPSAverageGrowthRate;
	}
	public void setbPSAverageGrowthRate(Float bPSAverageGrowthRate) {
		this.bPSAverageGrowthRate = bPSAverageGrowthRate;
	}
	public String getSector_Name() {
		return sector_Name;
	}
	public void setSector_Name(String sector_Name) {
		this.sector_Name = sector_Name;
	}
	public Float getrSI() {
		return rSI;
	}
	public void setrSI(Float rSI) {
		this.rSI = rSI;
	}
	
	
	
}
