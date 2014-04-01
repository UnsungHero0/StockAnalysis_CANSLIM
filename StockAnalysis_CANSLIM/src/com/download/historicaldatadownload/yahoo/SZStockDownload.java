package com.download.historicaldatadownload.yahoo;

//================================================================C
//  Web(http)から株価データを入手 (入力と同じ形で出力)
//    Http(Input):  http://table.yahoo.co.jp/t
//                  株価コード(nnnn)を指定する
//    Output File:  M-nnnn.txt    : プログラムと同じディレクトリ
//----------------------------------------------------------------C
//   取得月数の最大は50ヶ月 
//      Start date :  y1   = 2007;  m1    = 1;
//      End date   :  year = 2010;  month = 12;
//----------------------------------------------------------------C
//    javac HttpRead1.java  : classファイルの作成
//    java HttpRead1        : 実行   
//----------------------------------------------------------------C
//    Written by Yasunori Ushiro,  2011/10/31
//        ( Waseda University) 
//================================================================C
import java.net.*;
import java.io.*;
/*
 *
 */

public class SZStockDownload {
	/**
	 * sfdsjskfljasklfa
	 * @param args
	 * @author Daytona
	 */
	public static void main(String[] args) {
		try {
			File codefile = new File("RawData/Code and Name SZ.csv");
			FileInputStream fis = new FileInputStream(codefile);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String codeinfo;
			while ((codeinfo = br.readLine()) != null) {
				String code[] = codeinfo.split(",");
				String stockcode = code[0];
				//stockcode = "2160";
				int stockcodelength = stockcode.length();
				for (int i = 0; i < 6 - stockcodelength; i++)
					stockcode = "0" + stockcode;
				System.out.println(stockcode);
				URL url = new URL(
						"http://ichart.finance.yahoo.com/table.csv?s="
								+ stockcode
								+ ".SZ&a=03&b=12&c=1990&d=11&e=26&f=2013&g=d&ignore=.csv");
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.connect();
				BufferedReader fi = null;

				try {
				fi = new BufferedReader(new InputStreamReader(
						c.getInputStream()));
				
				FileWriter file = new FileWriter("result/" + stockcode
						+ "SZ.csv");
				BufferedWriter bw = new BufferedWriter(file);
				String nextline;
				while ((nextline = fi.readLine()) != null) {
					String item[] = nextline.split(",");
					for (String nextString : item)
						bw.write(nextString + ",");
					bw.write("\n");
					
					}
				bw.close();
				} catch (FileNotFoundException e) {
					System.out.println(stockcode + " is :  "+e.getMessage());
					}
				catch (NullPointerException e) {
					System.out.println(stockcode + " is :  "+e.getMessage());
				}
				
				fi.close();
				c.disconnect();
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			}

	}
}
