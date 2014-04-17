package module.quarterfinancialstatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import namespace.DBNameSpace;

public class FinancialStatementQuarterDownLoadKabuproAnalysisFetchRawData {

	public FinancialStatementQuarterDownLoadKabuproAnalysisFetchRawData() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap<String, FinancialStatementQuarterDownLoadKabuproAnalysisRecord> fetchRawDataFromDB(
			Connection con) {
		HashMap<String, FinancialStatementQuarterDownLoadKabuproAnalysisRecord> result = new HashMap<>();
		try {
			String sql = "Select * from "
					+ DBNameSpace.getQuarterfinancialstatementDb();
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			while (rs.next()) {
				if (result.containsKey(rs.getString("Local_Code"))) {
					FinancialStatementQuarterDownLoadKabuproAnalysisRecord record = addRecord(
							result.get(rs.getString("Local_Code")), rs);
					result.remove(rs.getString("Local_Code"));
					result.put(rs.getString("Local_Code"), record);
				} else {
					result.put(rs.getString("Local_Code"), getRecord(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Set<String> codeSet = result.keySet();
		for (String code : codeSet) {
			FinancialStatementQuarterDownLoadKabuproAnalysisRecord record = addIncresementData(result
					.get(code));
			// result.remove(code);
			result.put(code, record);
		}
		return result;
	}

	public static FinancialStatementQuarterDownLoadKabuproAnalysisRecord getRecord(
			ResultSet rs) {
		FinancialStatementQuarterDownLoadKabuproAnalysisRecord record = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord();

		try {
			record.setLocal_Code(rs.getString("Local_Code"));
			record.setCountry(rs.getString("Country"));
			record.setName_English(rs.getString("Name_English"));
			String period = rs.getString("Fiscal_Year").split("-")[0]
					+ "_Q"
					+ rs.getString("Period").substring(
							rs.getString("Period").length() - 1);
			record.getAccumulation_Income().put("Sales_" + period,
					rs.getFloat("Sales"));
			record.getAccumulation_Income().put("Net_Income_" + period,
					rs.getFloat("Net_Income"));
			record.getAccumulation_Income().put("Operating_Income_" + period,
					rs.getFloat("Operating_Income"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproAnalysisRecord addRecord(
			FinancialStatementQuarterDownLoadKabuproAnalysisRecord record,
			ResultSet rs) {
		try {
			String period = rs.getString("Fiscal_Year").split("-")[0]
					+ "_Q"
					+ rs.getString("Period").substring(
							rs.getString("Period").length() - 1);
			record.getAccumulation_Income().put("Sales_" + period,
					rs.getFloat("Sales"));
			record.getAccumulation_Income().put("Net_Income_" + period,
					rs.getFloat("Net_Income"));
			record.getAccumulation_Income().put("Operating_Income_" + period,
					rs.getFloat("Operating_Income"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproAnalysisRecord addIncresementData(
			FinancialStatementQuarterDownLoadKabuproAnalysisRecord record) {
		FinancialStatementQuarterDownLoadKabuproAnalysisRecord result = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord(
				record);
		// 1. insert Accumulation_Income_Increasement_Percent_Year
		HashMap<String, Float> Accumulation_Income_Increasement_Percent_Year = new HashMap<>();
		ArrayList<String> keyList = new ArrayList<>(record
				.getAccumulation_Income().keySet());
		while (!keyList.isEmpty()) {
			String key = keyList.get(0);
			HashMap<String, Float> tempMap = new HashMap<>();
			String quarter = getQuarter(key);
			tempMap.put(key, record.getAccumulation_Income().get(key));
			keyList.remove(key);
			for (String key2 : record.getAccumulation_Income().keySet()) {
				if (getQuarter(key2).equals(quarter)
						&& key2.split("_")[0].equals(key.split("_")[0])
						&& (!key.equals(key2))) {
					tempMap.put(key2, record.getAccumulation_Income().get(key2));
					keyList.remove(key2);
				}
			}
			for (String key2 : tempMap.keySet()) {
				String lastYearLabel = getLastYearLabel(key2);
				if (tempMap.containsKey(lastYearLabel) && tempMap.get(lastYearLabel)!=0) {
					Float increasePercent = (tempMap.get(key2) - tempMap
							.get(lastYearLabel)) / tempMap.get(lastYearLabel);
					Accumulation_Income_Increasement_Percent_Year.put(key2,
							increasePercent);
				}
			}
		}
		result.setAccumulation_Income_Increasement_Percent_Year(Accumulation_Income_Increasement_Percent_Year);
		record = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord(
				result);

		// 2. insert Season_Income
		HashMap<String, Float> Season_Income = new HashMap<>();
		keyList = new ArrayList<>(record.getAccumulation_Income().keySet());
		while (!keyList.isEmpty()) {
			String key = keyList.get(0);
			HashMap<String, Float> tempMap = new HashMap<>();
			tempMap.put(key, record.getAccumulation_Income().get(key));
			keyList.remove(key);
			for (String key2 : record.getAccumulation_Income().keySet()) {
				if (getYear(key).equals(getYear(key2))
						&& key2.split("_")[0].equals(key.split("_")[0])
						&& (!key.equals(key2))) {
					tempMap.put(key2, record.getAccumulation_Income().get(key2));
					keyList.remove(key2);
				}
			}
			for (String key2 : tempMap.keySet()) {
				if (key2.substring(key2.length() - 1).equals("1")) {
					Season_Income.put(key2, record.getAccumulation_Income()
							.get(key2));
				} else {
					String lastSeasonLabel = getLastSeasonLabel(key2);
					if (tempMap.containsKey(lastSeasonLabel)) {
						Float increase = (tempMap.get(key2) - tempMap
								.get(lastSeasonLabel));
						Season_Income.put(key2, increase);
					}
				}
			}
		}
		result.setSeason_Income(Season_Income);
		record = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord(
				result);

		// 3. insert Season_Income_Increasement_Percent_Season
		HashMap<String, Float> Season_Income_Increasement_Percent_Season = new HashMap<>();
		keyList = new ArrayList<>(record.getSeason_Income().keySet());
		while (!keyList.isEmpty()) {
			String key = keyList.get(0);
			HashMap<String, Float> tempMap = new HashMap<>();
			tempMap.put(key, record.getSeason_Income().get(key));
			keyList.remove(key);
			for (String key2 : record.getSeason_Income().keySet()) {
				if (getYear(key).equals(getYear(key2))
						&& key2.split("_")[0].equals(key.split("_")[0])
						&& (!key.equals(key2))) {
					tempMap.put(key2, record.getSeason_Income().get(key2));
					keyList.remove(key2);
				}
			}
			String tempLastLabel = "";
			for (String key2 : tempMap.keySet()) {
				if (getQuarter(key2).equals("1")) {
					tempLastLabel = getLastSeasonLabel(key2);
					break;
				}
			}
			if (record.getSeason_Income().containsKey(tempLastLabel)) {
				tempMap.put(tempLastLabel,
						record.getSeason_Income().get(tempLastLabel));
			}
			for (String key2 : tempMap.keySet()) {
				String lastSeasonLabel = getLastSeasonLabel(key2);
				if (tempMap.containsKey(lastSeasonLabel) && tempMap.get(lastSeasonLabel)!=0) {
					Float increasePercent = (tempMap.get(key2) - tempMap
							.get(lastSeasonLabel))
							/ tempMap.get(lastSeasonLabel);
					Season_Income_Increasement_Percent_Season.put(key2,
							increasePercent);
				}
			}
		}
		result.setSeason_Income_Increasement_Percent_Season(Season_Income_Increasement_Percent_Season);
		record = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord(
				result);

		// 4. insert Season_Income_Increasement_Percent_Year
		HashMap<String, Float> Season_Income_Increasement_Percent_Year = new HashMap<>();
		keyList = new ArrayList<>(record.getSeason_Income().keySet());
		while (!keyList.isEmpty()) {
			String key = keyList.get(0);
			HashMap<String, Float> tempMap = new HashMap<>();
			String quarter = getQuarter(key);
			tempMap.put(key, record.getSeason_Income().get(key));
			keyList.remove(key);
			for (String key2 : record.getSeason_Income().keySet()) {
				if (getQuarter(key2).equals(quarter)
						&& key2.split("_")[0].equals(key.split("_")[0])
						&& (!key.equals(key2))) {
					tempMap.put(key2, record.getSeason_Income().get(key2));
					keyList.remove(key2);
				}
			}
			for (String key2 : tempMap.keySet()) {
				String lastYearLabel = getLastYearLabel(key2);
				if (tempMap.containsKey(lastYearLabel) && tempMap.get(lastYearLabel) != 0) {
					Float increase = (tempMap.get(key2) - tempMap
							.get(lastYearLabel)) / tempMap.get(lastYearLabel);
					Season_Income_Increasement_Percent_Year.put(key2, increase);
				}
			}
		}
		result.setSeason_Income_Increasement_Percent_Year(Season_Income_Increasement_Percent_Year);
		record = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord(
				result);
		return record;
	}

	public static ArrayList<Entry<String, Float>> sortByYear(
			HashMap<String, Float> input) {
		ArrayList<Entry<String, Float>> record = new ArrayList<>(
				input.entrySet());
		Collections.sort(record, new Comparator<Entry<String, Float>>() {
			public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
				return o2.getKey().split("_")[o2.getKey().split("_").length - 2]
						.compareTo(o1.getKey().split("_")[o2.getKey()
								.split("_").length - 2]);
			}
		});
		return record;
	}

	public static String getYear(String input) {
		// 2015
		return input.split("_")[input.split("_").length - 2];
	}

	public static String getQuarter(String input) {
		// 1
		return input.split("_")[input.split("_").length - 1].substring(1);
	}

	public static String getLastYearLabel(String input) {
		String year = getYear(input);
		String quarter = getQuarter(input);
		String[] inputList = input.split("_");
		String result = "";
		for (int i = 0; i < inputList.length; i++) {
			if (!inputList[i].equals(year)) {
				result += inputList[i] + "_";
			} else {
				break;
			}
		}
		result += String.valueOf(Integer.valueOf(year) - 1) + "_Q" + quarter;
		return result;
	}

	public static String getLastSeasonLabel(String input) {
		String year = getYear(input);
		String quarter = getQuarter(input);
		String[] inputList = input.split("_");
		String result = "";
		for (int i = 0; i < inputList.length; i++) {
			if (!inputList[i].equals(year)) {
				result += inputList[i] + "_";
			} else {
				break;
			}
		}
		if (input.substring(input.length() - 1).equals("1")) {
			result += Integer.valueOf(year) - 1 + "_Q" + 4;
		} else {
			result += year
					+ "_Q"
					+ String.valueOf(Integer.valueOf(quarter.substring(quarter
							.length() - 1)) - 1);
		}
		return result;
	}

}
