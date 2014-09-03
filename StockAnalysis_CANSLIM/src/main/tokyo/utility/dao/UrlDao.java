package dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class UrlDao {

	private static Integer maxTry = 10;

	public UrlDao() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<String> getUrlBuffer(String urlString) {
		Integer tryTimes = 0;
		ArrayList<String> result = new ArrayList<>();
		BufferedReader fi = null;
		Boolean ifReaded = false;
		HttpURLConnection set = null;
		while (ifReaded == false) {
			try {
				if (tryTimes > maxTry) {
					throw new TooManyTryTimesException();
				}
				result = new ArrayList<>();
				URL url = new URL(urlString);
				set = (HttpURLConnection) url.openConnection();
				set.setReadTimeout(1000 * 30);
				set.connect();
				fi = new BufferedReader(new InputStreamReader(
						set.getInputStream()));
				String input = null;
				String charset = null;
				while ((input = fi.readLine()) != null) {
					if (input.contains("charset=")) {
						charset = input.substring(
								input.indexOf("charset=") + 8, input.length());
						Integer indexOfInvoke = charset.indexOf("\"");
						charset = charset.substring(0, indexOfInvoke);
						break;
					}
				}
				set = (HttpURLConnection) url.openConnection();

				if (charset != null) {
					fi = new BufferedReader(new InputStreamReader(
							set.getInputStream(), charset));
				} else {
					fi = new BufferedReader(new InputStreamReader(
							set.getInputStream()));
				}

				while ((input = fi.readLine()) != null) {
					result.add(input);
				}
				ifReaded = true;
			} catch (SocketTimeoutException e) {
				set.disconnect();
				System.out.println("time out");
				tryTimes++;
			} catch (ConnectException e) {
				set.disconnect();
				System.out.println("connection timeout");
				tryTimes++;
			} catch (IOException e) {
				if (e.toString().contains("java.io.FileNotFoundException")) {
					e.printStackTrace();
					ifReaded = true;
				} else {
					set.disconnect();
					e.printStackTrace();
					tryTimes++;
				}
			} catch (TooManyTryTimesException e) {
				e.printStackTrace();
				ifReaded = true;
			}
		}
		return result;
	}

	public static ArrayList<ArrayList<String>> getExcelFromUrl(String urlString) {
		Integer tryTimes = 0;
		String fileURL = urlString;
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		URLConnection conn = null;
		Boolean ifReaded = false;
		while (ifReaded == false) {
			try {
				if (tryTimes > maxTry) {
					throw new TooManyTryTimesException();
				}
				URL url = new URL(fileURL);
				conn = url.openConnection();
				conn.setReadTimeout(1000 * 30);
				InputStream is = conn.getInputStream();

				int irows = 0, icols = 0;
				Workbook rwb = Workbook.getWorkbook(is);

				Sheet rs = rwb.getSheet(0);
				irows = rs.getRows();
				icols = rs.getColumns();
				for (int i = 0; i < irows; i++) {
					ArrayList<String> oneline = new ArrayList<>();
					Boolean ifHasNext = false;
					for (int j = 0; j < icols; j++) {
						Cell cell = rs.getCell(j, i);
						String strc11 = cell.getContents();
						if (!strc11.trim().equals("")) {
							oneline.add(strc11);
							ifHasNext = true;
						}
					}
					if (ifHasNext == true) {
						result.add(oneline);
					} else {
						break;
					}
				}
				rwb.close();
				ifReaded = true;
			} catch (SocketTimeoutException e) {
				System.out.println("time out");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (TooManyTryTimesException e) {
				e.printStackTrace();
				ifReaded = true;
			}
		}
		return result;
	}
}

class TooManyTryTimesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TooManyTryTimesException() {
		super();
	}

	public TooManyTryTimesException(String msg) {
		super(msg);
	}
}
