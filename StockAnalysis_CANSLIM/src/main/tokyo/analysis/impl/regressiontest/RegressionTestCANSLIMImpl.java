package impl.regressiontest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import module.regressiontest.RegressionTestReportSummary;

/**
 * regression test for CANSLIM 1. set the start year, three years after year
 * whose financial statement records are saved in DB is preferable 2. set the
 * test duration, 6 months or 12 months or 18 months 3. the largest selected
 * stocks amount in each section is 3 4. the revenue is calculated by price_end
 * / price_start 5. the report is formatted as : 1. Local_Code, Name_English,
 * Start_date, End_date, Start_Price, End_Price, Revenue, Highest_Price,
 * Highest_Revenue, Lowest_Price, Lowest_Revenue
 * 
 * @author Daytona
 * 
 */

public class RegressionTestCANSLIMImpl {

	public RegressionTestCANSLIMImpl() {
	}

	public static void main(String[] args) {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		run();
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Long timeDif = endTime - startTime;
		Integer hour = Integer.valueOf((timeDif / (1000 * 60 * 60)) + "");
		Integer minute = Integer.valueOf((timeDif % (1000 * 60 * 60))
				/ (1000 * 60) + "");
		Integer second = Integer.valueOf(timeDif % (1000 * 60) + "");
		System.out.println("over!");
		System.out.println("running time is: " + hour + " hours " + minute
				+ " minutes " + second + " seconds.");
	}

	public static void run() {
		//find the proper startDate
		
		//set the loop group, e.g. startDate, duration
		
		//start the test
		
		//TODO
		
	}
	
	public static String findTestStartDate(Connection con){
		String result = "";
		//TODO
		return result;
	}
	
	public static HashMap<String, Integer> setLoop(Connection con){
		HashMap<String, Integer> result = new HashMap<>();
		//TODO
		return result;
	}
	
	public static RegressionTestReportSummary startTest(HashMap<String, Integer> testSet, Connection con){
		return null;
	}
	
	

}
