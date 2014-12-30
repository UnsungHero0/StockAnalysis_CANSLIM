package test;

// to import shanghai future exchange data into database from excel

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import net.sourceforge.pinyin4j.PinyinHelper;
import dao.UrlDao;

public class test4 {

	public static void main(String args[]) throws UnsupportedEncodingException {

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		startTime.set(2010, 3, 16);
		System.out.println(startTime.get(Calendar.YEAR) + "/"
				+ (startTime.get(Calendar.MONTH) + 1) + "/"
				+ startTime.get(Calendar.DAY_OF_MONTH));

		String url = "http://www.cffex.com.cn/fzjy/ccpm/201412/10/IF.xml";
		ArrayList<String> result = UrlDao.getUrlBuffer(url, "GBK");
		for (String ele : result) {
			// System.out.println(ele);
			if (ele.contains("Text")) {
//				System.out.println(ele.trim());
				System.out.print("Volume_Type: ");
				ele = ele
						.trim()
						.substring(ele.indexOf("Value") + 6,
								ele.indexOf("Value") + 7).trim();
				System.out.println(ele + "-OVER");
			} else if (ele.contains("instrumentId")) {
//				System.out.println(ele.trim());
				System.out.print("Instrument_Id: ");
				String[] eleArray = ele.trim().split("instrumentId>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				System.out.println(ele + "-OVER");
			} else if (ele.contains("rank")) {
//				System.out.println(ele.trim());
				System.out.print("Rank: ");
				String[] eleArray = ele.trim().split("rank>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				System.out.println(ele + "-OVER");
			} else if (ele.contains("volume>")) {
//				System.out.println(ele.trim());
				System.out.print("Volume: ");
				String[] eleArray = ele.trim().split("volume>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				System.out.println(ele + "-OVER");
			} else if (ele.contains("varVolume")) {
//				System.out.println(ele.trim());
				System.out.print("Volume_Variation: ");
				String[] eleArray = ele.trim().split("varVolume>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				System.out.println(ele + "-OVER");
			} else if (ele.contains("partyid")) {
//				System.out.println(ele.trim());
				System.out.print("Party_Id: ");
				String[] eleArray = ele.trim().split("partyid>");
				ele = eleArray[1].trim()
						.substring(0, eleArray[1].trim().indexOf("<")).trim();
				System.out.println(ele + "-OVER");
			}

			if (ele.contains("shortname")) {
				System.out.print("Party_Name: ");
				String gbkString = (ele.trim().substring(11)).substring(0,
						(ele.trim().substring(11)).indexOf("<"));
				System.out.print(gbkString + " ");
				char[] ch = gbkString.trim().toCharArray();
				for (char hanzi : ch) {
					String[] pinyinHead = PinyinHelper
							.toHanyuPinyinStringArray(hanzi);
					char[] pingying = pinyinHead[0].toCharArray();
					for (int i = 0; i < pingying.length; i++) {
						if (!Character.isDigit(pingying[i])) {
							if (i == 0) {
								System.out.print(String.valueOf(pingying[i])
										.toUpperCase());
							} else {
								System.out.print(pingying[i]);
							}
						}
					}
				}
				System.out.println();
			}
		}

	}

}
