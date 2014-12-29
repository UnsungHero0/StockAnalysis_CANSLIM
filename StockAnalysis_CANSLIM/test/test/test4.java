package test;

// to import shanghai future exchange data into database from excel

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import dao.UrlDao;
import tool.charDeal;
import jxl.Sheet;
import jxl.Workbook;

public class test4 {

	public static void main(String args[]) throws UnsupportedEncodingException {

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		startTime.set(2010, 3, 16);
		System.out.println(startTime.get(Calendar.YEAR) + "/"
				+ (startTime.get(Calendar.MONTH) + 1) + "/"
				+ startTime.get(Calendar.DAY_OF_MONTH));

		String url = "http://www.cffex.com.cn/fzjy/ccpm/201412/10/IF.xml";
		ArrayList<String> result = UrlDao.getUrlBuffer(url);
		for (String ele : result) {
			if (ele.contains("shortname")) {
				String gbkString = (ele.trim().substring(11)).substring(0,
						(ele.trim().substring(11)).indexOf("<"));
				String utfString = new String(gbkString.getBytes(), "GBK");
				System.out.println(utfString);
			}
		}

	}

}
