package impl.download;

/**
 * the volume is not adjusted after the stock split
 * this program is used for adjust the volume , so that we evaluate the true volume
 *
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import namespace.DBNameSpace;
import commontool.JDBCUtil;
import dao.UrlDao;
import datasource.DataSourceUtil;
import module.shareholdingsplitinfo.HistoricalDataDownloadVolumeSplitCreateTableAndInsertData;
import module.shareholdingsplitinfo.HistoricalDataDownloadVolumeSplitRecord;
import jdbcdao.CodeListsDao;

public class HistoricalDataDownloadVolumeSplitImpl {

	public HistoricalDataDownloadVolumeSplitImpl() {

	}

	public static void main(String args[]) {
		ArrayList<String> codeList = new ArrayList<>();
		codeList = new CodeListsDao().getCodeListsFromFinancialStatement();
		ArrayList<HistoricalDataDownloadVolumeSplitRecord> result = new ArrayList<>();
		Connection con = null;
		for (String code : codeList) {
			HistoricalDataDownloadVolumeSplitRecord record = new HistoricalDataDownloadVolumeSplitRecord();
			record.setLocal_Code(code);
			HashMap<String, Float> splitInfo = fetchInfoFromUrl(code);
			record.setSplitHistory(splitInfo);
			result.add(record);
			Set<String> keySet = record.getSplitHistory().keySet();
			
			for (String key : keySet) {
				System.out.println(record.getLocal_Code() + "  " + key + "  "
						+ record.getSplitHistory().get(key));
			}
			
			System.out.println("---  " + code + " is ok, " + (codeList.size() - codeList.indexOf(code)) + " to go ");
		}
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			String tableName = DBNameSpace.getHistoricalstocksplitDb();
			JDBCUtil.dropTable(tableName, con);
			HistoricalDataDownloadVolumeSplitCreateTableAndInsertData.createTableAndInsertData(result, con);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("over");
	}

	public static HashMap<String, Float> fetchInfoFromUrl(String code) {
		HashMap<String, Float> resultMap = new HashMap<>();
		String urlString = "http://stocks.finance.yahoo.co.jp/stocks/chart/?code="
				+ code + ".T";
		ArrayList<String> result = UrlDao.getUrlBuffer(urlString);
		for (String string : result) {
			if (string.contains("分割情報")) {
				string = string.substring(string.indexOf("<li>"));
				String stringList[] = string.split("<strong>");
				for (String subString : stringList) {
					if (subString.contains("greyFin")) {
						/*
						System.out.print(code + " :   ");
						System.out.print(subString.substring(
								subString.indexOf(":") + 1,
								subString.indexOf("]"))
								+ "  |  ");
						System.out.print(subString.substring(
								subString.indexOf("（") + 1,
								(subString.indexOf("）")))
								+ "  ||||  ");
								*/
						try {
							String inputDate = subString.substring(
									subString.indexOf("（") + 1,
									(subString.indexOf("）")));
							Float rate = Float.valueOf(subString.substring(
									subString.indexOf(":") + 1,
									subString.indexOf("]")));
							SimpleDateFormat sdfInput = new SimpleDateFormat(
									"yy/MM/dd");
							Date date = (Date) sdfInput.parse(inputDate);
							SimpleDateFormat sdfOutput = new SimpleDateFormat(
									"yyyy-MM-dd");
							String outputDate = sdfOutput.format(date);
							resultMap.put(outputDate, rate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return resultMap;
	}

}
