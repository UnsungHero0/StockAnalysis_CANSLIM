package module.canslimanalysis;

import java.util.ArrayList;

public class FinancialStatementAnalysisRecord {
	//Array earlier -> later
	private String Local_Code = "";
	private String Form = "";
	private ArrayList<Float> salesArray = new ArrayList<>();
	private ArrayList<Float> salesGrowthRateArray = new ArrayList<>();
	private Float salesAverageGrowthRate = 0.0f;
	private ArrayList<Float> ePSArray = new ArrayList<>();
	private ArrayList<Float> ePSGrowthRateArray = new ArrayList<>();
	private Float ePSAverageGrowthRate = 0f;
	private ArrayList<Float> bPSArray = new ArrayList<>();
	private ArrayList<Float> bPSGrowthRateArray = new ArrayList<>();
	private Float bPSAverageGrowthRate = 0f;
	private String sector_Name = "";
	private Float rSIInAllStock = 0f;
	private Float rSIInDepartment = 0f;
	private ArrayList<Float> salesQuarterArray = new ArrayList<>();
	private ArrayList<Float> salesQuarterGrowthRateArray = new ArrayList<>();
	private Float salesQuarterAverageGrowthRate = 0f;
	private ArrayList<Float> net_IncomeQuarterArray = new ArrayList<>();
	private ArrayList<Float> net_IncomeQuarterGrowthRateArray = new ArrayList<>();
	private Float net_IncomeQuarterAverageGrowthRate = 0f;
	private Double FiftyWeekAverageVolume = 0.0;
	private Float todayToFiftyWeeksAverage = 0f;
	private Float ePSAveGrowthRateToPER = 0f;
	
	public ArrayList<Float> getSalesQuarterArray() {
		return salesQuarterArray;
	}

	public void setSalesQuarterArray(ArrayList<Float> salesQuarterArray) {
		this.salesQuarterArray = salesQuarterArray;
	}

	public ArrayList<Float> getSalesQuarterGrowthRateArray() {
		return salesQuarterGrowthRateArray;
	}

	public void setSalesQuarterGrowthRateArray(
			ArrayList<Float> salesQuarterGrowthRateArray) {
		this.salesQuarterGrowthRateArray = salesQuarterGrowthRateArray;
	}

	public Float getSalesQuarterAverageGrowthRate() {
		return salesQuarterAverageGrowthRate;
	}

	public void setSalesQuarterAverageGrowthRate(Float salesQuarterAverageGrowthRate) {
		this.salesQuarterAverageGrowthRate = salesQuarterAverageGrowthRate;
	}

	public ArrayList<Float> getNet_IncomeQuarterArray() {
		return net_IncomeQuarterArray;
	}

	public void setNet_IncomeQuarterArray(ArrayList<Float> net_IncomeQuarterArray) {
		this.net_IncomeQuarterArray = net_IncomeQuarterArray;
	}

	public ArrayList<Float> getNet_IncomeQuarterGrowthRateArray() {
		return net_IncomeQuarterGrowthRateArray;
	}

	public void setNet_IncomeQuarterGrowthRateArray(
			ArrayList<Float> net_IncomeQuarterGrowthRateArray) {
		this.net_IncomeQuarterGrowthRateArray = net_IncomeQuarterGrowthRateArray;
	}

	public Float getNet_IncomeQuarterAverageGrowthRate() {
		return net_IncomeQuarterAverageGrowthRate;
	}

	public void setNet_IncomeQuarterAverageGrowthRate(
			Float net_IncomeQuarterAverageGrowthRate) {
		this.net_IncomeQuarterAverageGrowthRate = net_IncomeQuarterAverageGrowthRate;
	}

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


	public Double getFiftyWeekAverageVolume() {
		return FiftyWeekAverageVolume;
	}

	public void setFiftyWeekAverageVolume(Double fiftyWeekAverageVolume) {
		FiftyWeekAverageVolume = fiftyWeekAverageVolume;
	}

	public Float getTodayToFiftyWeeksAverage() {
		return todayToFiftyWeeksAverage;
	}

	public void setTodayToFiftyWeeksAverage(Float todayToFiftyWeeksAverage) {
		this.todayToFiftyWeeksAverage = todayToFiftyWeeksAverage;
	}

	public Float getePSAveGrowthRateToPER() {
		return ePSAveGrowthRateToPER;
	}

	public void setePSAveGrowthRateToPER(Float ePSAveGrowthRateToPER) {
		this.ePSAveGrowthRateToPER = ePSAveGrowthRateToPER;
	}

	public FinancialStatementAnalysisRecord() {
		// TODO Auto-generated constructor stub
	}

}
