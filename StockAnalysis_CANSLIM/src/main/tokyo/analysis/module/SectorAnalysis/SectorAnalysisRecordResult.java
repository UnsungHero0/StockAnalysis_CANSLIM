package module.SectorAnalysis;

import java.util.HashMap;

public class SectorAnalysisRecordResult {
	HashMap<Integer, SectorAnalysisRecord> result = new HashMap<>();

	public HashMap<Integer, SectorAnalysisRecord> getResult() {
		return result;
	}

	public void setResult(HashMap<Integer, SectorAnalysisRecord> result) {
		this.result = result;
	}

}
