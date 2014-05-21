package impl.update;

import java.util.Calendar;

import module.ListOfTSEListed.ListOfTSEListedUpdate;
import module.shareholding.ShareHoldingUpdateMultiThread;

public class TotalUpdate {
	
	public TotalUpdate(){
		
	}
	
	public static void main(String args[]){
		
		run();
		
	}
	
	public static void run(){
		Long startTime = Calendar.getInstance().getTimeInMillis();
		
		//ListOfTSEListedUpdateImpl.run();
		//HistoricalQuoteUpdateMultiThreadVersion.run(8);
		//FinancialStatementYahooUpdateMultiThreadVersion.run(8);
		ShareHoldingSplitUpdateMultiThreadVersion.run();
		ShareHoldingUpdateMultiThread.run(8);
		
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
	}
}
