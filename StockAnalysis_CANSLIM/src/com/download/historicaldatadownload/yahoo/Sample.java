package com.download.historicaldatadownload.yahoo;

//================================================================C
//Web(http)‚©‚çŠ”‰¿ƒf[ƒ^‚ð“üŽè  (“ü—Í‚Æ“¯‚¶Œ`‚Åo—Í)
//Http(Input):  http://table.yahoo.co.jp/t
//              Š”‰¿ƒR[ƒh(nnnn)‚ðŽw’è‚·‚é
//Output File:  M-nnnn.txt    : ƒvƒƒOƒ‰ƒ€‚Æ“¯‚¶ƒfƒBƒŒƒNƒgƒŠ
//----------------------------------------------------------------C
//Žæ“¾ŒŽ”‚ÌÅ‘å‚Í50ƒ–ŒŽ 
//  Start date :  y1   = 2007;  m1    = 1;
//  End date   :  year = 2010;  month = 12;
//----------------------------------------------------------------C
//javac HttpRead1.java  : classƒtƒ@ƒCƒ‹‚Ìì¬
//java HttpRead1        : ŽÀs   
//----------------------------------------------------------------C
//Written by Yasunori Ushiro (Œã@•Û”Í),  2011/10/31
//    ( Waseda University, ‘ˆî“c‘åŠw ) 
//================================================================C
import java.net.*;
import java.io.*;
import java.util.*;

public class Sample {
	public static void main(String[] args) throws MalformedURLException,
			ProtocolException, IOException {
		String[] Ary, Wk;
		String input, str, str1;
		String[] Ar1, Ar2;
		String small = "<small>";
		int count = 1;
		int year, month, y1, m1, cy, cm, nmonth, end = 0;
		// Set start and end date (year, month)
		y1 = 2007;
		m1 = 0;
		year = 2012;
		month = 12;
		nmonth = (year - y1) * 12 + month - m1 + 1;
		// Input stock-code
		System.out.println("start");
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
		String code = br.readLine();
		// Http Connection and Input Buffer
		cy = year;
		cm = month;

		/*
		 * URL url = new URL("http://table.yahoo.co.jp/t?c=" + y1 + "&a=" + m1 +
		 * "&b=1" + "&f=" + year + "&d=" + month + "&e=31" + "&g=d&s=" + code +
		 * "&ignore=.csv");
		 */
		// Output File
		FileOutputStream fo = new FileOutputStream("M-" + code + "1.csv");
		PrintWriter q = new PrintWriter(fo, true);
		for (int j = 4; j < 6; j++) {
			int no = j * 50;
			URL url = new URL(
					"http://table.yahoo.co.jp/t?s=1301.t&a=1&b=1&c=1990&d=12&e=31&f=2013&g=d&q=t&y="
							+ no + "&z=1301.t&x=.csv");

			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			set.setRequestProperty("Accept-Language", "jp");
			set.connect();
			BufferedReader fi = new BufferedReader(new InputStreamReader(
					set.getInputStream()));
			
			
			// Http File Read
			while (true) {
				input = fi.readLine();
				if (input == null) {
					break;
				}
				if (input.startsWith("</tr></table>")) {
					if (end == 1) {
						break;
					} else {
						end = 1;
					}
				}
				// <td><small> Š”‰¿ƒf[ƒ^ </small>
				if (input.startsWith("<td>")) {
					if (input.indexOf(small) != -1)
					// Å‰‚Ì6ƒf[ƒ^‚ÍŠ”‰¿ˆÈŠOAÅŒã‚Ì”ƒf[ƒ^‚àŠ”‰¿ˆÈŠO
					{
						if (count >= 7) {
							Wk = input.split(small);
							Ary = Wk[1].split("</small>");
							int ct7 = count % 7;
							// cs7=1 --> Žn’l, 2-->‚’l, 3-->ˆÀ’l, 6-->I’l
							Ar1 = Ary[0].split(",");
							String st = Ar1[0];
							// System.out.println(st);
							for (int i = 1; i < Ar1.length; i++) {
								st = st + Ar1[i];
							}
							if (ct7 == 1) {
								q.print(st + ",");
								System.out.println(no);
								System.out.println(url);
							}
							if (ct7 == 2) {
								q.print(st + ",");
							}
							if (ct7 == 3) {
								try {
									st = st.substring(3, st.length() - 4);
									q.print(st + ",");
								} catch (StringIndexOutOfBoundsException e) {
									e.printStackTrace();
									System.out.println(j);
									System.out.println(url);
								}
							}
							if (ct7 == 4) {
								q.print(st + ",");
							}
							if (ct7 == 5) {
								q.print(st + ",");
							}
							if (ct7 == 6 && Character.isDigit(st.charAt(0))) {
								st = "";
								for (String string : Ary) {
									char[] number = string.toCharArray();
									for (char num : number) {
										if (Character.isDigit(num)) {
											st += String.valueOf(num);
										} else if (!Character.isDigit(num)
												&& !String
														.valueOf(
																st.charAt(st
																		.length() - 1))
														.equals("/")) {
											st += "/";
										}
									}

								}
								st = st.substring(0, st.length() - 1);
								//System.out.println(st);
								q.println(st);
							}
							count++;
						} else {
							count++;
						}
					}
				}
			}
		set.disconnect();
		fi.close();
		}
		fo.close();
		q.close();
		in.close();
		br.close();
	
	}
}
