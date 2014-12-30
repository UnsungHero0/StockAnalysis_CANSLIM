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

public class ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadImpl
		extends consolePrint {

	public static void impl(Connection con) {
		// 1. read files from URL
		ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> input = getInputDataFromURL();

		// 2. turn into MySql input format
		ArrayList<String> inputData = null;
		println("data read is finished");

		// 3. delete table if exists
		// deleteTable(con);

		// 4. create table in MySql
		// createTable(con);

		// 5. input data
		// insertDataIntoDB(inputData, con);
		// println("future exchange quotes download finished");

	}

	public static ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> getInputDataFromURL() {
		ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> input = new ArrayList<>();
		return input;
	}

	public static ArrayList<String> turningFromat(
			ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> input) {
		ArrayList<String> result = new ArrayList<>();
		return result;
	}

	public static void deleteTable(Connection con) {
		JDBCUtil.dropTable(
				ShanghaiDBNameSpace.getFutureexchangehistoricalquoteDb(), con);
	}

	public static void createTable(Connection con) {
		String tableName = ShanghaiDBNameSpace
				.getFutureexchangehistoricalquoteDb();
		String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName
				+ " (" + "`Country` VARCHAR(50) NOT NULL Default 'Shanghai',"
				+ "`Future_Code` VARCHAR(10) NOT NULL,"
				+ "`Date` DATE NOT NULL," + "`Preclose` Double,"
				+ "`Presettle` Double," + "`Open` Double," + "`High` Double,"
				+ "`Low` Double," + "`Close` Double," + "`Settle` Double,"
				+ "`Change1` Double," + "`Change2` Double,"
				+ "`Volume` Double," + "`Volume_Figure` Double,"
				+ "`Inventory` Double," + "PRIMARY KEY (`Future_code`,`Date`))";
		JDBCUtil.excuteQuery(createTableSql, con);
	}

	public static void insertDataIntoDB(ArrayList<String> input, Connection con) {
		String tableName = ShanghaiDBNameSpace
				.getFutureexchangehistoricalquoteDb();
		for (String str : input) {
			String insertDataSql = "INSERT INTO "
					+ tableName
					+ " "
					+ "(Future_Code, Date, Preclose, Presettle, Open, High, Low, Close, Settle, Change1, Change2, Volume, Volume_Figure, Inventory) VALUES "
					+ str;
			JDBCUtil.excuteQuery(insertDataSql, con);
		}
	}

	public static String turnListIntoMySqlFormatWithNoBracket(
			ArrayList<String> input) {
		String result = "";
		for (String str : input) {
			result += str + ",";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	public static String turnListIntoMySqlFormatWithBracket(
			ArrayList<String> input) {
		String result = "(";
		for (String str : input) {
			if (str.length() == 0) {
				result += "null,";
			} else {
				result += "'" + str + "',";
			}
		}
		result = result.substring(0, result.length() - 1) + ")";
		return result;
	}

	public static ArrayList<String> divideMysqlInput(String input) {
		ArrayList<String> result = new ArrayList<>();
		Integer groupNumber = 10;
		Integer firstIndex = 0;
		for (int i = 1; i <= groupNumber; i++) {
			Integer lastIndex = (input.length() / groupNumber) * (i);
			while (!input.substring(lastIndex, lastIndex + 1).equals(")")) {
				lastIndex++;
			}

			result.add(input.substring(firstIndex, lastIndex + 1));
			if (i != groupNumber) {
				firstIndex = lastIndex + 2;
			}
		}
		return result;
	}
}

class ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer {
	private String date = null;
	private String contract_ID = null;
	private String type = null;
	private Integer rank = null;
	private Double volume = null;
	private Double change = null;

	public ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer() {
		// TODO Auto-generated constructor stub
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContract_ID() {
		return contract_ID;
	}

	public void setContract_ID(String contract_ID) {
		this.contract_ID = contract_ID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getChange() {
		return change;
	}

	public void setChange(Double change) {
		this.change = change;
	}
}
