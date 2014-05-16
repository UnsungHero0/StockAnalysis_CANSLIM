package dao;

import java.io.BufferedReader;
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
import module.ListOfTSEListed.ListOfTSEListedFileURL;

public class UrlDao {

	public UrlDao() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<String> getUrlBuffer(String urlString) {
		ArrayList<String> result = new ArrayList<>();
		BufferedReader fi = null;
		Boolean ifReaded = false;
		HttpURLConnection set = null;
		while (ifReaded == false) {
			try {
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
				fi = new BufferedReader(new InputStreamReader(
						set.getInputStream(), charset));
				while ((input = fi.readLine()) != null) {
					result.add(input);
				}
				ifReaded = true;
			} catch (SocketTimeoutException e) {
				set.disconnect();
				System.out.println("time out");
			} catch (ConnectException e) {
				set.disconnect();
				System.out.println("connection timeout");
			} catch (IOException e) {
				set.disconnect();
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static ArrayList<ArrayList<String>> getExcelFromUrl(String urlString) {
		String fileURL = urlString;
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		URLConnection conn = null;
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return result;
	}
}
