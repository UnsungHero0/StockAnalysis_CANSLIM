package module.SectorAnalysis;

import java.util.ArrayList;


public class SectorAnalysisRecord {
	
	private Integer sectorCode = 0;
	private String sectorName = "";
	private Integer sectorRSI = 0;
	private Integer sectorWeightRSI = 0;
	private Double sectorValue = 0.0;
	private Float sectorEPS = 0f;
	private Float sectorWeightEPS = 0f;
	private Float sectorPER = 0f;
	private Float sectorWeightPER = 0f;
	private ArrayList<String> codeList = new ArrayList<>();
	
	public Integer getSectorCode() {
		return sectorCode;
	}
	public void setSectorCode(Integer sectorCode) {
		this.sectorCode = sectorCode;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public Integer getSectorRSI() {
		return sectorRSI;
	}
	public void setSectorRSI(Integer sectorRSI) {
		this.sectorRSI = sectorRSI;
	}
	public Double getSectorValue() {
		return sectorValue;
	}
	public void setSectorValue(Double sectorValue) {
		this.sectorValue = sectorValue;
	}
	public Float getSectorEPS() {
		return sectorEPS;
	}
	public void setSectorEPS(Float sectorEPS) {
		this.sectorEPS = sectorEPS;
	}
	public Float getSectorPER() {
		return sectorPER;
	}
	public void setSectorPER(Float sectorPER) {
		this.sectorPER = sectorPER;
	}
	public ArrayList<String> getCodeList() {
		return codeList;
	}
	public void setCodeList(ArrayList<String> codeList) {
		this.codeList = codeList;
	}
	public Float getSectorWeightEPS() {
		return sectorWeightEPS;
	}
	public void setSectorWeightEPS(Float sectorWeightEPS) {
		this.sectorWeightEPS = sectorWeightEPS;
	}
	public Float getSectorWeightPER() {
		return sectorWeightPER;
	}
	public void setSectorWeightPER(Float sectorWeightPER) {
		this.sectorWeightPER = sectorWeightPER;
	}
	public Integer getSectorWeightRSI() {
		return sectorWeightRSI;
	}
	public void setSectorWeightRSI(Integer sectorWeightRSI) {
		this.sectorWeightRSI = sectorWeightRSI;
	}
	
	
}
