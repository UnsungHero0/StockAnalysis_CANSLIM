package module.shanghaifutureexchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;

import namespace.ShanghaiDBNameSpace;
import commontool.JDBCUtil;
import jxl.Sheet;
import jxl.Workbook;
import tool.charDeal;
import tool.consolePrint;

public class ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl extends consolePrint{

	public static void impl(Connection con) {
//		1. read files from excel
		ArrayList<ArrayList<String>> input = getInputDataFromExcel();

//		2. turn into MySql input format
		String inputData = turningFromat(input);
		println("data read is finished");
		println(inputData);

//		3. delete table if exists
		deleteTable(con);

//		4. create table in MySql
		createTable(con);

//		5. input data
		insertDataIntoDB(inputData, con);

	}

	public static ArrayList<ArrayList<String>> getInputDataFromExcel() {
		ArrayList<ArrayList<String>> input = new ArrayList<>();
		try {
			String years[] = { "2011", "2012", "2013", "2014" };
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
						} else if ((i == 0 && j > 3)
								&& !rs.getCell(i, j).getContents().equals("")) {
							item = rs.getCell(i, j).getContents();
							oneInput.add(item);
						} else if ((i == 0 && j > 3)
								&& rs.getCell(i, j).getContents().equals("")) {
							oneInput.add(item);
						} else {
							String strc = charDeal.subComma(rs.getCell(i, j)
									.getContents());
							oneInput.add(strc);
						}
					}
					input.add(oneInput);
				}
			}
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
		JDBCUtil.dropTable(ShanghaiDBNameSpace.getFutureexchangehistoricalquoteDb(), con);
	}

	public static void createTable(Connection con) {
		String tableName = ShanghaiDBNameSpace.getFutureexchangehistoricalquoteDb();
		String createTableSql = "CREATE TABLE IF NOT EXISTS "
				+ tableName + " ("
				+ "`Country` VARCHAR(50) NOT NULL Default 'Shanghai',"
				+ "`Future_Code` VARCHAR(10) NOT NULL,"
				+"`Date` DATE NOT NULL," 
				+"`Preclose` Double,"
				+"`Presettle` Double,"
				+ "`Open` Double,"
				+ "`High` Double," 
				+ "`Low` Double,"
				+ "`Close` Double,"
				+ "`Settle` Double," 
				+ "`Change1` Double,"
				+ "`Change2` Double," 
				+ "`Volume` Double," 
				+ "`Volume_Figure` Double," 
				+ "`Inventory` Double," 
				+ "PRIMARY KEY (`Future_code`,`Date`))";
		JDBCUtil.excuteQuery(createTableSql, con);
	}
	
	public static void insertDataIntoDB(String input, Connection con) {
		String tableName = ShanghaiDBNameSpace.getFutureexchangehistoricalquoteDb();
		String insertDataSql = "INSERT INTO " + tableName + " "
				+ "(Future_Code, Date, Preclose, Presettle, Open, High, Low, Close, Settle, Change1, Change2, Volume, Volume_Figure, Inventory) VALUES "
				+ input;
		println(insertDataSql);
		JDBCUtil.excuteQuery(insertDataSql, con);
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
			if (str.length() ==0) {
				result += "null,";
			} else {
			result += "'" + str + "',";
			}
		}
		result = result.substring(0,result.length()-1) + ")";
		return result;
	}

}
