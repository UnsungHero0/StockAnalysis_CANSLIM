package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuotesTest {

	public QuotesTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		String code = "1400";
		String getQuotesUrl = "http://info.finance.yahoo.co.jp/history/?code="
				+ code + ".T&sy=" + "1983" + "&sm=" + "1" + "&sd=" + "1"
				+ "&ey=" + "2014" + "&em=" + "3" + "&ed=" + "31" + "&tm=d&p="
				+ "1";
		try {
			URL url = new URL(getQuotesUrl);
			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			set.setRequestProperty("Accept-Language", "jp");
			set.disconnect();
			set.connect();
			BufferedReader fi = new BufferedReader(new InputStreamReader(
					set.getInputStream()));
			String input = "";
			while ((input = fi.readLine()) != null) {
				if (input.contains("分割") && input.startsWith("</tr>"))
					System.out.println(input);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
