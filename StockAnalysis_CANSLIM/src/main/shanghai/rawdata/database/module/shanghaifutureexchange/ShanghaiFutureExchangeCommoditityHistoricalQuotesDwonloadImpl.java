package module.shanghaifutureexchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import tool.charDeal;

public class ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl {

	public static void impl(Connection con) {
		// 1. read files from excel
		ArrayList<ArrayList<String>> input = getInputDataFromExcel();

		// 2. turn into MySql input format
		String inputData = turningFromat(input);
		System.out.println(inputData);

		// 3. delete table if exists
		deleteTable(con);

		// 4. create table in MySql
		createTable(con);

		// 5. input data
		insertDataIntoDB(inputData, con);

	}

	public static ArrayList<ArrayList<String>> getInputDataFromExcel() {
		ArrayList<ArrayList<String>> input = new ArrayList<>();
		try {
//			String years[] = { "2011", "2012", "2013", "2014" };
			String years[] = { "2011"};
			for (String yr : years) {
				File sourcefile = new File(
						"./rawData/"
								+ yr + "ShanghaiCommoditiyPrice.xls");
				InputStream is = new FileInputStream(sourcefile);
				jxl.Workbook rwb = Workbook.getWorkbook(is);
				Sheet rs = rwb.getSheet(0);
				String item = "";
				for (int j = 3; j < rs.getRows() - 6; j++) {
					ArrayList<String> oneInput = new ArrayList<>();
					for (int i = 0; i < rs.getColumns() - 1; i++) {
						if (i == 0 && j == 3) {
							item = rs.getCell(i, j).getContents();
							oneInput.add(item);
							// System.out.print(item + "/");
						} else if ((i == 0 && j > 3)
								&& !rs.getCell(i, j).getContents().equals("")) {
							item = rs.getCell(i, j).getContents();
							oneInput.add(item);
							// System.out.print(item + "/");
						} else if ((i == 0 && j > 3)
								&& rs.getCell(i, j).getContents().equals("")) {
							oneInput.add(item);
							// System.out.print(item + "/");
						} else {
							String strc = charDeal.subComma(rs.getCell(i, j)
									.getContents());
							oneInput.add(strc);
							// System.out.print(strc + "/");
						}
					}
					input.add(oneInput);
					// System.out.println();
				}
			}
//			for (ArrayList<String> ele : input) {
//				for (String str : ele) {
//					System.out.print(str + ",");
//				}
//				System.out.println();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	public static String turningFromat(
			ArrayList<ArrayList<String>> input) {
		ArrayList<String> output = new ArrayList<>();
		for (ArrayList<String> ele : input) {
			String strin = turnListIntoMySqlFormatWithBracket(ele);
			output.add(strin);
		}
		
		return turnListIntoMySqlFormatWithNoBracket(output);
	}

	public static void deleteTable(Connection con) {
		// TODO
	}

	public static void createTable(Connection con) {
		// TODO
	}

	public static void insertDataIntoDB(String input, Connection con) {
		// TODO
	}
	
	public static String turnListIntoMySqlFormatWithNoBracket(ArrayList<String> input) {
		String result = "";
		for (String str : input) {
			result += str + ",";
		}
		result = result.substring(0,result.length()-1);
		return result;
	}
	
	public static String turnListIntoMySqlFormatWithBracket(ArrayList<String> input) {
		String result = "(";
		for (String str : input) {
			result += str + ",";
		}
		result = result.substring(0,result.length()-1) + ")";
		return result;
	}

}
