package impl.download;

import java.util.Calendar;

import module.shareholding.ShareHoldingDownloadMultiThread;

public class ShareHoldingDownloadImpl {
	
	public static void main(String args[]) {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		
		ShareHoldingDownloadMultiThread.run(8);
		
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
	}
}
