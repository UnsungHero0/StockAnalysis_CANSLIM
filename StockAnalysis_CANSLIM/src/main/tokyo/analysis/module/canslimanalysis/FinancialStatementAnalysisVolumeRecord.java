package module.canslimanalysis;

import java.util.HashMap;

public class FinancialStatementAnalysisVolumeRecord {
	
	private String local_Code = "";
	private HashMap<String, Double> splitInfo = new HashMap<>();
	
	public String getLocal_Code() {
		return local_Code;
	}
	public void setLocal_Code(String local_Code) {
		this.local_Code = local_Code;
	}
	public HashMap<String, Double> getSplitInfo() {
		return splitInfo;
	}
	public void setSplitInfo(HashMap<String, Double> splitInfo) {
		this.splitInfo = splitInfo;
	}
}
