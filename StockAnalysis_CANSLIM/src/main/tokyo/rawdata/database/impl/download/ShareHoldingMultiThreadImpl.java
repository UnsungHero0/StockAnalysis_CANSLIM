package impl.download;

/**download the latest share holding number from yahoo.co.jp
 * @author Daytona
 */

import java.util.Calendar;

import module.shareholding.ShareHoldingDownloadMultiThread;

public class ShareHoldingMultiThreadImpl {
	
	public ShareHoldingMultiThreadImpl(){
		
	}
	
	public static void main(String args[]) {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		run();
		System.out.println("shareHolding download is finished!");
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
	}
	
	public static void run(){
		Integer threadNumber = 8;
		ShareHoldingDownloadMultiThread.downLoad(threadNumber);
	}

}
