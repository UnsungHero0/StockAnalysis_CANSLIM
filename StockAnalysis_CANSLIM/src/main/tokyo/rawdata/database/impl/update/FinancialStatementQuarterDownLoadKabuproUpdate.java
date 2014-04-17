package impl.update;

/**
 * 1.getCodeList, and getUrl info
 * 2. get the first record, sort as annoucement_date the identification word is code+fiscalyear+period (1) 
 * or code+fiscalyear_Prediction+period (2)
 * 3. if no same identification (1), insert into DB, otherwise, stop to the next code
 * 4. if has same identification (2), compare the announcement date, 
 * delete the old duplicated one, and insert new one
 * @author Daytona
 *
 */
public class FinancialStatementQuarterDownLoadKabuproUpdate {

	public FinancialStatementQuarterDownLoadKabuproUpdate() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) {
		//TODO
	}

}
