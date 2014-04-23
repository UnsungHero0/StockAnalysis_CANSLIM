package module.canslimanalysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.sql.DataSource;

import datasource.DataSourceUtil;

public class FinancialStatementAnalysisSales {

	public FinancialStatementAnalysisSales() {
		// TODO Auto-generated constructor stub
	}
	
	public HashMap<String, FinancialStatementAnalysisRecord> getSales(
			HashMap<String, FinancialStatementAnalysisRecord> record, Connection con) {
		// getcodelist from financialstatement
		// eps into arraylist

		// calculate the growthrate

		// HashMap key code+form value growthrate
		Set<String> keySet = record.keySet();
		Integer allRecordNumber = keySet.size();
		Integer count = 0;
		Float percente = (float) 0;
		Integer i = 1;
		for (String code : keySet) {
			ArrayList<Float> salesArray = new ArrayList<>();
			salesArray = getSalesArray(Integer.valueOf(code), "independent", con);
			/*
			ArrayList<Float> bPSArray = new ArrayList<>();
			bPSArray = getBPSArray(Integer.valueOf(code), "independent", con);
			*/
			if (salesArray.size() >= 2) {
				record.get(code).setSalesArray(salesArray);
				record.get(code).setForm("independent");
				Float salesRate = getAverageGrowthRate(salesArray);
				record.get(code).setSalesAverageGrowthRate(salesRate);
				record.get(code).setSalesGrowthRateArray(
						getGrowthRateArray(salesArray));
				/*
				Float bPSRate = getGrowthRate(bPSArray);
				record.get(code).setbPSAverageGrowthRate(bPSRate);
				record.get(code).setbPSGrowthRateArray(
						getGowthRateArray(bPSArray));
						*/
			}
			/*
			 * ePSArray = getEPSArray(Integer.valueOf(code), "consolidate");
			 * bPSArray = getBPSArray(Integer.valueOf(code), "consolidate"); if
			 * (ePSArray.size() >= 2) { FinancialStatementAnalysisRecord record
			 * = new FinancialStatementAnalysisRecord();
			 * record.setePSArray(ePSArray); record.setForm("consolidate");
			 * record.setLocal_Code(code); Float rate = getGrowthRate(ePSArray);
			 * record.setePSAverageGrowthRate(rate);
			 * record.setePSGrowthRateArray(getGowthRateArray(ePSArray)); Float
			 * bPSRate = getGrowthRate(bPSArray);
			 * record.setbPSAverageGrowthRate(bPSRate);
			 * record.setbPSGrowthRateArray(getGowthRateArray(bPSArray));
			 * result.add(record); }
			 */

			percente = ((float) count++ / (float) allRecordNumber);
			if (percente * 10 > 1 * i) {
				System.out.print((int) (percente * 100) + "%-> ");
				i++;
			}
		}
		System.out.println("100%");

		// output
		/*
		 * for (FinancialStatementAnalysisRecord record : result) { if
		 * (satisfyCANSLIM(record)) { System.out.println(record.getLocal_Code()
		 * + " " + record.getForm() + " " + ((int)
		 * (record.getePSAverageGrowthRate() * 100)) + "% " + ((int)
		 * (record.getbPSAverageGrowthRate() * 100)) + "%");
		 * System.out.print("EPS : "); for (int j = 0; j <
		 * record.getePSGrowthRateArray().size(); j++) { System.out.print((int)
		 * (record.getePSGrowthRateArray().get( j) * 100) + "% "); }
		 * System.out.println(); System.out.print("BPS : "); for (int j = 0; j <
		 * record.getbPSGrowthRateArray().size(); j++) { System.out.print((int)
		 * (record.getbPSGrowthRateArray().get( j) * 100) + "% "); }
		 * System.out.println(); } } System.out.println("finished");
		 */

		return record;
	}

	public static ArrayList<Float> getSalesArray(Integer code, String type, Connection con) {
		ArrayList<Float> result = new ArrayList<>();
		try {
			String selectEPSRecord = "SELECT Fiscal_Year, Sales FROM FinancialStatementTokyo_test WHERE "
					+ "Local_Code = "
					+ code
					+ " AND Form = "
					+ "'"
					+ type
					+ "' ORDER BY Fiscal_Year";
			ResultSet rs = con.prepareStatement(selectEPSRecord).executeQuery();
			while (rs.next()) {
				result.add(rs.getFloat("Sales"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return result;
	}

	public static ArrayList<Float> getBPSArray(Integer code, String type, Connection con) {
		ArrayList<Float> result = new ArrayList<>();
		try {
			String selectEPSRecord = "SELECT Fiscal_Year, BPS FROM FinancialStatementTokyo_test WHERE "
					+ "Local_Code = "
					+ code
					+ " AND Form = "
					+ "'"
					+ type
					+ "' ORDER BY Fiscal_Year";
			ResultSet rs = con.prepareStatement(selectEPSRecord).executeQuery();
			while (rs.next()) {
				result.add(rs.getFloat("BPS"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return result;
	}

	public static ArrayList<Float> getGrowthRateArray(ArrayList<Float> input) {
		ArrayList<Float> result = new ArrayList<>();
		Float rate = (float) 0;
		for (int i = 0; i < input.size() - 1; i++) {
			if (input.get(i) != 0)
				rate = ((input.get(i + 1) - input.get(i))
						/ Math.abs(input.get(i)));
			result.add(rate);
		}
		return result;
	}

	public static Float getAverageGrowthRate(ArrayList<Float> input) {
		Float rate = (float) 0;
		for (int i = 0; i < input.size() - 1; i++) {
			if (input.get(i) != 0)
				rate = rate + (input.get(i + 1) - input.get(i))
						/ Math.abs(input.get(i));
		}
		rate = rate / (input.size() - 1);
		return rate;
	}

	public static Boolean satisfyCANSLIM(FinancialStatementAnalysisRecord record) {
		Integer count = 0;
		for (int i = 0; i < record.getePSGrowthRateArray().size(); i++) {
			if (record.getePSGrowthRateArray().get(i) > 0.25) {
				count++;
			}
		}
		if (count == record.getePSGrowthRateArray().size()
				&& record.getePSArray().get(record.getePSArray().size() - 1) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static DataSource getDataSource() {
		return DataSourceUtil.getTokyoDataSourceRoot();
	}


}
