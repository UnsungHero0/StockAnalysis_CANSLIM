package module.canslimanalysis;

import java.text.Collator;
import java.util.Comparator;

public class FinancialStatementAnalysisComparator implements Comparator<FinancialStatementAnalysisRecord> {

	Collator collator = Collator.getInstance();
	public int compare(FinancialStatementAnalysisRecord element1, FinancialStatementAnalysisRecord element2) {
	    return element2.getePSAverageGrowthRate().toString().compareTo(element1.getePSAverageGrowthRate().toString());
	}
}
