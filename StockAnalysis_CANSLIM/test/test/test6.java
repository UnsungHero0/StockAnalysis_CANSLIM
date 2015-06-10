package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.print.attribute.standard.OutputDeviceAssigned;

import module.canslimanalysis.FinancialStatementAnalysisRecord;
import module.listedcompanydownload.DownLoadListedCompanyNewYork;
import commontool.JDBCUtil;
import dao.DBNewYorkDao;
import dao.DBSydenyDao;
import dao.DateDao;
import dao.UrlDao;
import datasource.DataSourceUtil;
import tool.consolePrint;

public class test6 {

	private static Connection con = null;

	public static void main(String args[]) {
		start();
	}

	public static void start() {
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			startTest(con);

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
	}

	public static void startTest(Connection con) {
		HashMap<String, ArrayList<Float>> FinanalResult = new HashMap<>();
		ArrayList<String> codeList = getOldListedCompanyFromDB(con);
		for (int j = 0; j < codeList.size(); j++) {
			String code = codeList.get(j);
			String query = "Select Date, Open, Close From sydneyexchange."
					+ code + "_historicalquotes_sydney";
			HashMap<String, ArrayList<String>> result = new HashMap<>();
			ResultSet rs = null;
			try {
				rs = JDBCUtil.excuteQueryWithResult(query, con);
				while (rs.next()) {
					ArrayList<String> oneLine = new ArrayList<>();
					oneLine.add(rs.getString("Date"));
					oneLine.add(rs.getString("Open"));
					oneLine.add(rs.getString("Close"));
					result.put(rs.getString("Date"), oneLine);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (java.lang.NullPointerException e) {
				e.printStackTrace();
				continue;
			}
			ArrayList<String> dateList = new ArrayList<>(result.keySet());

			Collections.sort(dateList, new Comparator<String>() {
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});

			Float sum = 0.0f;
			Float a00Count = 0.0f;
			Float a01Count = 0.0f;
			Float a10Count = 0.0f;
			Float a11Count = 0.0f;

			for (int i = 1; i < dateList.size(); i++) {
				Float openTodayCloseYesterday = Float.valueOf(result.get(
						dateList.get(i)).get(1))
						- Float.valueOf(result.get(dateList.get(i - 1)).get(2));
				Float closeTodayOpenToday = Float.valueOf(result.get(
						dateList.get(i)).get(2))
						- Float.valueOf(result.get(dateList.get(i)).get(1));
				if (openTodayCloseYesterday > 0 && closeTodayOpenToday > 0) {
					a00Count += 1;
					sum += 1;
				} else if (openTodayCloseYesterday > 0
						&& closeTodayOpenToday < 0) {
					a01Count += 1;
					sum += 1;
				} else if (openTodayCloseYesterday < 0
						&& closeTodayOpenToday > 0) {
					a10Count += 1;
					sum += 1;
				} else if (openTodayCloseYesterday < 0
						&& closeTodayOpenToday < 0) {
					a11Count += 1;
					sum += 1;
				}

			}
			consolePrint.print(code + " result : ");
			consolePrint.print("00 is " + a00Count / sum + " , ");
			consolePrint.print("01 is " + a01Count / sum + " , ");
			consolePrint.print("10 is " + a10Count / sum + " , ");
			consolePrint.println("11 is " + a11Count / sum
					+ " , left number is "
					+ (codeList.size() - codeList.indexOf(code)));
			ArrayList<Float> calculateResult = new ArrayList<>();
			calculateResult.add(a00Count / sum);
			calculateResult.add(a01Count / sum);
			calculateResult.add(a10Count / sum);
			calculateResult.add(a11Count / sum);
			query = "Select avg(abs(Open-Close)/Open) avg From sydneyexchange."
					+ code + "_historicalquotes_sydney";
			try {
				rs = JDBCUtil.excuteQueryWithResult(query, con);
				while (rs.next()) {
					calculateResult.add(Float.valueOf(rs.getString("avg")));
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (java.lang.NullPointerException e) {
				e.printStackTrace();
				continue;
			}

			FinanalResult.put(code, calculateResult);
		}
		output(FinanalResult);
		consolePrint.println("finished");

	}

	private static ArrayList<String> getOldListedCompanyFromDB(Connection con) {
//		return DBNewYorkDao.getCodeListFromDB(con);
		 return DBSydenyDao.getCodeListFromDB(con);
	}

	private static void output(HashMap<String, ArrayList<Float>> result) {
		SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		Calendar calendar = Calendar.getInstance();
		String fileName = "." + File.separator + "result_"
				+ sdfFile.format(calendar.getTime()) + ".csv";
		BufferedWriter bw = null;

		File file = new File(fileName);
		consolePrint.println(fileName);

		try {
			// write csv record to output file
			file.createNewFile();
			OutputStream writeStream = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(writeStream, "SJIS"));
			bw.write("Code,No,ZeroZero,ZeroOne,OneZero,OneOne,ZeroZero/(ZeroZero+ZeroOne),OneZero/(OneZero+OneOne),Average Change ");
			bw.newLine();
			DecimalFormat df = new DecimalFormat("0.##");
			Integer No = 1;
			for (String code : result.keySet()) {
				bw.write(doubleQuoteToken(code)
						+ ","
						+ doubleQuoteToken(String.valueOf(No))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								0)))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								1)))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								2)))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								3)))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								0)
								/ (result.get(code).get(0) + result.get(code)
										.get(1))))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								2)
								/ (result.get(code).get(2) + result.get(code)
										.get(3))))
						+ ","
						+ doubleQuoteToken(String.valueOf(result.get(code).get(
								4))));
				bw.newLine();
				No += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	private static String doubleQuoteToken(String token) {
		return "\"" + token + "\"";
	}

	@SuppressWarnings("serial")
	class CsvWriterException extends RuntimeException {
		public CsvWriterException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
