package impl.update;

import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import module.shareholding.ShareHoldingUpdateMultiThread;

public class ShareHoldingUpdateImpl {
	
	public static void main(String args[]) {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		
		ShareHoldingUpdateMultiThread.run(8);
		
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
		try {
			Context ctx = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
