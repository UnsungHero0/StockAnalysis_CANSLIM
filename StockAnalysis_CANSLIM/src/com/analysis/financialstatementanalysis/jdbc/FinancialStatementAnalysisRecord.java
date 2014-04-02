package com.analysis.financialstatementanalysis.jdbc;

import java.util.ArrayList;

public class FinancialStatementAnalysisRecord {
	
	private String Local_Code = "";
	private String Form = "";
	private ArrayList<Float> salesArray = new ArrayList<>();
	private ArrayList<Float> salesGrowthRateArray = new ArrayList<>();
	private Float salesAverageGrowthRate = 0.0f;
	private ArrayList<Float> ePSArray = new ArrayList<>();
	private ArrayList<Float> ePSGrowthRateArray = new ArrayList<>();
	private Float ePSAverageGrowthRate = (float) 0.0;
	private ArrayList<Float> bPSArray = new ArrayList<>();
	private ArrayList<Float> bPSGrowthRateArray = new ArrayList<>();
	private Float bPSAverageGrowthRate = (float) 0.0;
	private String sector_Name = "";
	private Float rSIInAllStock = (float) 0.0;
	private Float rSIInDepartment = (float) 0.0;
	
	public ArrayList<Float> getSalesArray() {
		return salesArray;
	}

	public void setSalesArray(ArrayList<Float> salesArray) {
		this.salesArray = salesArray;
	}

	public ArrayList<Float> getSalesGrowthRateArray() {
		return salesGrowthRateArray;
	}

	public void setSalesGrowthRateArray(ArrayList<Float> salesGrowthRateArray) {
		this.salesGrowthRateArray = salesGrowthRateArray;
	}


	public Float getSalesAverageGrowthRate() {
		return salesAverageGrowthRate;
	}


	public void setSalesAverageGrowthRate(Float salesAverageGrowthRate) {
		this.salesAverageGrowthRate = salesAverageGrowthRate;
	}


	public Float getrSIInAllStock() {
		return rSIInAllStock;
	}


	public void setrSIInAllStock(Float rSIInAllStock) {
		this.rSIInAllStock = rSIInAllStock;
	}


	public Float getrSIInDepartment() {
		return rSIInDepartment;
	}


	public void setrSIInDepartment(Float rSIInDepartment) {
		this.rSIInDepartment = rSIInDepartment;
	}
	
	public String getLocal_Code() {
		return Local_Code;
	}


	public void setLocal_Code(String local_Code) {
		Local_Code = local_Code;
	}


	public String getForm() {
		return Form;
	}


	public void setForm(String form) {
		Form = form;
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


	public Float getRSIInAllStock() {
		return rSIInAllStock;
	}


	public void setRSIInAllStock(
			Float rSIStrengthInAllStock) {
		this.rSIInAllStock = rSIStrengthInAllStock;
	}


	public Float getRSIStrengthInDepartment() {
		return rSIInDepartment;
	}


	public void setRSIInDepartment(
			Float rSIStrengthInDepartment) {
		this.rSIInDepartment = rSIStrengthInDepartment;
	}


	public FinancialStatementAnalysisRecord() {
		// TODO Auto-generated constructor stub
	}

}
