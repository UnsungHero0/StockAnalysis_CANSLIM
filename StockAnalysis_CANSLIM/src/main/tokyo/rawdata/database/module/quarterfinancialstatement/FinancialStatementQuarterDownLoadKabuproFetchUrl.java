package module.quarterfinancialstatement;

/**
 * 1. get information of fiscal year, period, sales, operation income, net income
 * 2. if fiscal year contains ("予、修") -> preRecrod, it should be prediction item
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dao.UrlDao;

public class FinancialStatementQuarterDownLoadKabuproFetchUrl {

	public FinancialStatementQuarterDownLoadKabuproFetchUrl() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<FinancialStatementQuarterDownLoadKabuproRecord> setRecordFromURL(
			FinancialStatementQuarterDownLoadKabuproRecord inputRecord) {
		ArrayList<FinancialStatementQuarterDownLoadKabuproRecord> result = new ArrayList<>();
		String urlString = "http://ke.kabupro.jp/xbrl/"
				+ inputRecord.getLocal_Code() + ".htm";
		ArrayList<String> inputArray = UrlDao.getUrlBuffer(urlString);
		HashMap<String, FinancialStatementQuarterDownLoadKabuproRecord> preRecord = new HashMap<>();
		for (String input : inputArray) {
			if (input.contains("リクエストされたページがみつかりませんでした")) {
				result = null;
			}
		}
		Integer i = 0;
		FinancialStatementQuarterDownLoadKabuproRecord newRecord = new FinancialStatementQuarterDownLoadKabuproRecord();
		newRecord = cloneRecord(inputRecord);
		if (inputRecord != null) {
			for (String input : inputArray) {
				if (input.contains("<tr>") && input.contains("pdf")) {
					String inputList[] = input.split("</a>");
					for (String element : inputList) {
						if (element.contains("htm")) {
							i = 0;
							element = element
									.substring(element.indexOf("htm") + 4);
							setValueIndex4(element, newRecord);
							if (!(newRecord.getFiscal_Year().contains("修") || newRecord
									.getFiscal_Year().contains("予"))) {
								result.add(newRecord);
								// System.out.println(newRecord.getFieldsForSqlDB());
							} else {
								preRecord.put(newRecord.getAnnouncement_Date(),
										newRecord);
								// System.out.println(newRecord.getFieldsForSqlDB());
							}
							newRecord = new FinancialStatementQuarterDownLoadKabuproRecord();
							newRecord = cloneRecord(inputRecord);
							break;
						}
						element = element.substring(element.indexOf("pdf") + 4)
								.trim();
						if (element.contains(">")) {
							element = element.substring(
									element.indexOf(">") + 1).trim();
						}
						if (!(element.equals("　") || element.contains("</tr")
								|| element.contains("業績予想") || element
									.contains("%"))) {
							if (element.contains("-")) {
								element = "-"
										+ element
												.substring(2, element.length());
							}
							if (element.contains("htm")) {
								element = element.substring(element
										.indexOf("htm") + 4);
							}
							while (element.contains(",")) {
								element = element.substring(0,
										element.indexOf(","))
										+ element.substring(
												element.indexOf(",") + 1,
												element.length());
							}
							String methodName = "setValueIndex" + i;
							try {
								Method m = FinancialStatementQuarterDownLoadKabuproFetchUrl.class
										.getMethod(
												methodName,
												String.class,
												FinancialStatementQuarterDownLoadKabuproRecord.class);
								m.invoke(
										FinancialStatementQuarterDownLoadKabuproFetchUrl.class,
										element, newRecord);
							} catch (NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							} catch (IllegalAccessException
									| IllegalArgumentException
									| InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						i++;
					}
				}
			}
			Set<String> setKey = preRecord.keySet();
			String maxDate = "0";
			if (setKey.size() != 0) {
				for (String key : setKey) {
					if (Integer.valueOf(maxDate) < Integer.valueOf(key)) {
						maxDate = key;
					}
				}
				preRecord.get(maxDate).setFiscal_Year(
						preRecord.get(maxDate).getFiscal_Year().substring(3, 7)
								+ "0101");
				result.add(preRecord.get(maxDate));
			}
		}
		/*
		 * for (FinancialStatementQuarterDownLoadKabuproRecord record : result)
		 * { System.out.println(record.getValuesForSqlDB()); }
		 */
		return result;
	}

	// TODO
	public static FinancialStatementQuarterDownLoadKabuproRecord setValueIndex0(
			String input, FinancialStatementQuarterDownLoadKabuproRecord record) {
		record.setFiscal_Year(input.substring(0, 4));
		if (input.contains("通期")) {
			record.setPeriod("Quarter4");
			record.setFiscal_Year(input.substring(0, 4) + "0101");
		} else if (input.contains("第3")) {
			record.setPeriod("Quarter3");
			record.setFiscal_Year(input.substring(0, 4) + "0101");
		} else if (input.contains("第2")) {
			record.setPeriod("Quarter2");
			record.setFiscal_Year(input.substring(0, 4) + "0101");
		} else if (input.contains("第1")) {
			record.setPeriod("Quarter1");
			record.setFiscal_Year(input.substring(0, 4) + "0101");
		}
		if (!(input.contains("修") || input.contains("予"))) {
			record.setType("Declared");
		} else if (input.contains("予")) {
			record.setType("Prediction");
		} else if (input.contains("修")) {
			record.setType("Fixed");
		}
		record.setFiscal_Year(input);
		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproRecord setValueIndex1(
			String input, FinancialStatementQuarterDownLoadKabuproRecord record) {
		record.setSales(Long.valueOf(input + "000000"));
		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproRecord setValueIndex2(
			String input, FinancialStatementQuarterDownLoadKabuproRecord record) {
		record.setOperating_Income(Long.valueOf(input + "000000"));
		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproRecord setValueIndex3(
			String input, FinancialStatementQuarterDownLoadKabuproRecord record) {
		record.setNet_Income(Long.valueOf(input + "000000"));
		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproRecord setValueIndex4(
			String input, FinancialStatementQuarterDownLoadKabuproRecord record) {
		String date[] = input.split("/");
		String dateString = "";
		for (int i = 0; i < date.length; i++) {
			dateString = dateString + date[i];
		}
		record.setAnnouncement_Date(dateString);
		return record;
	}

	public static FinancialStatementQuarterDownLoadKabuproRecord cloneRecord(
			FinancialStatementQuarterDownLoadKabuproRecord input) {
		FinancialStatementQuarterDownLoadKabuproRecord result = new FinancialStatementQuarterDownLoadKabuproRecord();
		result.setCountry(input.getCountry());
		result.setLocal_Code(input.getLocal_Code());
		result.setName_English(input.getName_English());
		return result;
	}

}
