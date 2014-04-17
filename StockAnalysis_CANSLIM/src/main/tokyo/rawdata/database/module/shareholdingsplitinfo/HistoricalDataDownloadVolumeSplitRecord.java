package module.shareholdingsplitinfo;

import java.util.HashMap;

public class HistoricalDataDownloadVolumeSplitRecord {
	
	private String Local_Code = "";
	// key date, value split rate
	private HashMap<String, Float> splitHistory = new HashMap<>();
	
	public HistoricalDataDownloadVolumeSplitRecord() {
		
	}

	public String getLocal_Code() {
		return Local_Code;
	}

	public void setLocal_Code(String local_Code) {
		Local_Code = local_Code;
	}

	public HashMap<String, Float> getSplitHistory() {
		return splitHistory;
	}

	public void setSplitHistory(HashMap<String, Float> splitHistory) {
		this.splitHistory = splitHistory;
	}
}
