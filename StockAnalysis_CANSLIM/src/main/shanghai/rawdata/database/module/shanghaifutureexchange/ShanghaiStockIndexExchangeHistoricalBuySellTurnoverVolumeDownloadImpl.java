package module.shanghaifutureexchange;

import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import namespace.ShanghaiDBNameSpace;
import net.sourceforge.pinyin4j.PinyinHelper;
import commontool.DateOperation;
import commontool.JDBCUtil;
import dao.DateDao;
import dao.UrlDao;
import tool.consolePrint;

public class ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadImpl
		extends consolePrint {

	public static void impl(Connection con) {

		// 1. delete table if exists
//		deleteTable(con);

		// 2. create table in MySql
//		createTable(con);

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		startTime.set(2010, 8, 11);
		// startTime.set(2014, 11, 25);
		while (!DateOperation.ifSameDate(startTime, endTime)) {
			if (!DateOperation.ifWeekend(startTime)) {

				System.out.println(startTime.get(Calendar.YEAR) + "/"
						+ (startTime.get(Calendar.MONTH) + 1) + "/"
						+ startTime.get(Calendar.DATE) + " start");
				// 3. read files from URL
				ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> input = getInputDataFromURL(startTime);

				// 4. turn into MySql input format
				ArrayList<String> inputData = turningFromat(input);

				// 5. input data
				insertDataIntoDB(inputData, con);
				System.out.println(startTime.get(Calendar.YEAR) + "/"
						+ (startTime.get(Calendar.MONTH) + 1) + "/"
						+ startTime.get(Calendar.DATE) + " finished");
			}
			startTime.add(Calendar.DATE, 1);
			// sleep for 10 seconds
			long timeout = 10 * 1000;
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e1) {
			}
		}
		println("Stock index future volume rank download finished");

	}

	public static ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> getInputDataFromURL(
			Calendar date) {
		ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> records = new ArrayList<>();
		String month = (date.get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = (date.get(Calendar.DAY_OF_MONTH)) + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		String url = "http://www.cffex.com.cn/fzjy/ccpm/"
				+ date.get(Calendar.YEAR) + month + "/" + day + "/IF.xml";
		ArrayList<String> urlContent = UrlDao.getUrlBuffer(url, "GBK");
		// System.out.println(url);
		if (whetherHasData(urlContent)) {
			for (ArrayList<String> oneSection : splitIntoSection(urlContent)) {
				records.add(extractRecordFromText(oneSection, date));
			}
		}
		return records;
	}

	/**
	 * check whether the date has exchanging record
	 * 
	 * @param input
	 * @return
	 */
	public static Boolean whetherHasData(ArrayList<String> input) {
		Boolean result = true;
		for (String ele : input) {
			if (ele.contains("Transitional")) {
				result = false;
				break;
			}
		}
		return result;
	}

	public static ArrayList<ArrayList<String>> splitIntoSection(
			ArrayList<String> input) {
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		ArrayList<String> sectionContent = new ArrayList<>();
		Boolean flag = false;
		for (String ele : input) {
			ele.trim();
			if (ele.contains("<data  Text")) {
				flag = true;
				sectionContent = new ArrayList<>();
				sectionContent.add(ele);
				// System.out.println(ele);
			} else if (ele.contains("</data>")) {
				result.add(sectionContent);
				flag = false;
			} else if (flag.equals(true)) {
				sectionContent.add(ele);
				// System.out.println(ele);
			}
		}
		return result;
	}

	public static ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer extractRecordFromText(
			ArrayList<String> input, Calendar date) {
		ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer newRecord = new ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer();
		newRecord.setDate(DateDao.dateIntoMysqlForm(date));
		for (String ele : input) {
			if (ele.contains("Text")) {
				ele = ele
						.trim()
						.substring(ele.indexOf("Value") + 6,
								ele.indexOf("Value") + 7).trim();
				if (ele.equals("0")) {
					newRecord.setType("Turnover");
				} else if (ele.equals("1")) {
					newRecord.setType("Buy");
				} else if (ele.equals("2")) {
					newRecord.setType("Sell");
				}
			} else if (ele.contains("instrumentId")) {
				String[] eleArray = ele.trim().split("instrumentId>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				newRecord.setInstrumentId(ele);
			} else if (ele.contains("rank")) {
				String[] eleArray = ele.trim().split("rank>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				newRecord.setRank(Integer.valueOf(ele));
			} else if (ele.contains("volume>")) {
				String[] eleArray = ele.trim().split("volume>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				newRecord.setVolume(Double.valueOf(ele));
			} else if (ele.contains("varVolume")) {
				String[] eleArray = ele.trim().split("varVolume>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				// System.out.println(ele + "-OVER");
				newRecord.setVolumeVariation(Double.valueOf(ele));
			} else if (ele.contains("partyid")) {
				String[] eleArray = ele.trim().split("partyid>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				newRecord.setPartyId(ele);
			}

			if (ele.contains("shortname")) {
				String gbkString = (ele.trim().substring(11)).substring(0,
						(ele.trim().substring(11)).indexOf("<"));
				char[] ch = gbkString.trim().toCharArray();
				String name = "";
				for (char hanzi : ch) {
					String[] pinyinHead = PinyinHelper
							.toHanyuPinyinStringArray(hanzi);
					char[] pingying = pinyinHead[0].toCharArray();
					for (int i = 0; i < pingying.length; i++) {
						if (!Character.isDigit(pingying[i])) {
							if (i == 0) {
								name += String.valueOf(pingying[i])
										.toUpperCase();
							} else {
								name += pingying[i];
							}
						}
					}
				}
				newRecord.setPartyName(name);
			}
		}
		return newRecord;
	}

	public static ArrayList<String> turningFromat(
			ArrayList<ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer> input) {
		ArrayList<String> result = new ArrayList<>();

		for (int i = 0; i < input.size(); i++) {
			String oneResultArray = "";
			oneResultArray += "(\'" + input.get(i).getDate() + "\',";
			oneResultArray += "\'" + input.get(i).getInstrumentId() + "\',";
			oneResultArray += "\'" + input.get(i).getPartyName() + "\',";
			oneResultArray += input.get(i).getPartyId() + ",";
			oneResultArray += "\'" + input.get(i).getType() + "\',";
			oneResultArray += input.get(i).getRank() + ",";
			oneResultArray += input.get(i).getVolume() + ",";
			oneResultArray += input.get(i).getVolumeVariation() + "),";
			result.add(oneResultArray.substring(0, oneResultArray.length() - 1));
		}
		return result;
	}

	public static void deleteTable(Connection con) {
		JDBCUtil.dropTable(
				ShanghaiDBNameSpace.getStockindexfuturevolumerankDb(), con);
	}

	public static void createTable(Connection con) {
		String tableName = ShanghaiDBNameSpace
				.getStockindexfuturevolumerankDb();
		String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName
				+ " (" + "`Country` VARCHAR(50) NOT NULL Default 'Shanghai',"
				+ "`Date` DATE NOT NULL,"
				+ "`Instrument_Id` VARCHAR(10) NOT NULL,"
				+ "`Party_Name` VARCHAR(30) NOT NULL,"
				+ "`Party_Id` VARCHAR(10)," + "`Type` VARCHAR(10) NOT NULL,"
				+ "`Rank` Int NOT NULL," + "`Volume` Double,"
				+ "`Volume_Variation` Double,"
				+ "PRIMARY KEY (`Date`,`Instrument_Id`,`Party_Name`,`Type`))";
		JDBCUtil.excuteQuery(createTableSql, con);
	}

	public static void insertDataIntoDB(ArrayList<String> input, Connection con) {
		String tableName = ShanghaiDBNameSpace
				.getStockindexfuturevolumerankDb();
		for (String str : input) {
			String insertDataSql = "INSERT INTO "
					+ tableName
					+ " "
					+ "(Date, Instrument_Id, Party_Name, Party_Id, Type, Rank, Volume, Volume_Variation) VALUES "
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
	private String instrumentId = null;
	private String type = null;
	private String partyName = null;
	private String partyId = null;
	private Integer rank = null;
	private Double volume = null;
	private Double volumeVariation = null;

	public ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownloadRecordContainer() {
		// TODO Auto-generated constructor stub
	}

	public String getDate() {
		return date;
	}

	/**
	 * date
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	/**
	 * contractId, "IF1402" means this contract will be expired until February
	 * 2014
	 * 
	 * @param instrumentId
	 */
	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getType() {
		return type;
	}

	/**
	 * turnover/buy/sell
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getPartyName() {
		return partyName;
	}

	/**
	 * name of holder
	 * 
	 * @return
	 */
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getPartyId() {
		return partyId;
	}

	/**
	 * ID of holder
	 * 
	 * @param partyId
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Integer getRank() {
		return rank;
	}

	/**
	 * volume rank
	 * 
	 * @param rank
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Double getVolume() {
		return volume;
	}

	/**
	 * volume of turnover/buy/sell
	 * 
	 * @param volume
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getVolumeVariation() {
		return volumeVariation;
	}

	/**
	 * volume volume to the previous exchanging day
	 * 
	 * @param volumeVariation
	 */
	public void setVolumeVariation(Double volumeVariation) {
		this.volumeVariation = volumeVariation;
	}

	public void printout() {
		System.out.println(this.date);
		System.out.println(this.instrumentId);
		System.out.println(this.partyId);
		System.out.println(this.partyName);
		System.out.println(this.type);
		System.out.println(this.rank);
		System.out.println(this.volume);
		System.out.println(this.volumeVariation);
		System.out.println();
	}
}
