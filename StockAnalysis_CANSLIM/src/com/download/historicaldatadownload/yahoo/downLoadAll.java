package com.download.historicaldatadownload.yahoo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;

import org.omg.CORBA.TIMEOUT;

public class downLoadAll {

	public static void main(String args[]) {
		URL url;
		ArrayList<String> inputAddressList = getInputtedFiles("RawData");
		for (String inputAddress : inputAddressList) {
			String outputFolder = "Result";
			ArrayList<String> codeArrayList = readcode(inputAddress);
			int totalRecordNumber = getallnumber(inputAddress);
			int numberNow = 0;
			ArrayList<String> wrongList = new ArrayList<>();
			for (String code : codeArrayList) {
				//String code = "5935";
				numberNow++;
				System.out.println(ifOutputAlready(code, outputFolder));
				if (!ifOutputAlready(code, outputFolder)) {
					File file = null;
					try {
						Integer recordNumber = findRecordNumber(code);
						int loop = recordNumber / 50 + 1;
						System.out.println("Reading " + code + " from "
								+ inputAddress + ", page " + loop
								+ ", remained record "
								+ (totalRecordNumber - numberNow));
						file = new File(outputFolder + "/" + code + "T.csv");
						FileWriter fo = new FileWriter(file);
						BufferedWriter q = new BufferedWriter(fo);
						q.write("Date,Open,High,Low,Close,Volume,Adj Close");
						q.newLine();
						System.out.print("page: ");

						for (int i = 1; i <= loop; i++) {
							System.out.print(+i + " ");
							url = new URL(
									"http://info.finance.yahoo.co.jp/history/?code="
											+ code
											+ ".T&sy=1983&sm=1&sd=1&ey=2013&em=12&ed=31&tm=d&p="
											+ i);
							HttpURLConnection set = (HttpURLConnection) url
									.openConnection();
							set.setRequestProperty("Accept-Language", "jp");
							set.setReadTimeout(1000 * 30);
							BufferedReader fi = null;
							Boolean ifReaded = false;
							while (ifReaded.equals(false)) {
								try {
									set.disconnect();
									set = (HttpURLConnection) url
											.openConnection();
									set.setReadTimeout(1000 * 30);
									set.connect();
									fi = new BufferedReader(
											new InputStreamReader(
													set.getInputStream()));
									ifReaded = true;
								} catch (SocketTimeoutException e) {
									System.out.println("time out " + loop);
								}
							}

							String input = "";
							String string1 = "";
							while ((input = fi.readLine()) != null) {
								if (input.startsWith("</tr>")) {
									String inputRow[] = input
											.split("</tr><tr><td>");
									for (String string : inputRow) {
										try {
											if (Character.isDigit(string
													.charAt(0))) {
												if (string.contains("</table>")) {
													string = string
															.substring(
																	0,
																	string.length() - 13);
												}
												if (string.contains("class")) {
													string1 = string
															.substring(
																	0,
																	string.indexOf("class") - 9);
													string = string
															.substring(
																	string.indexOf("\">") + 6,
																	string.length() - 1);
													writer(q, string1);
													q.write("\n");
												}
												writer(q, string);
												q.write("\n");
											}
										} catch (StringIndexOutOfBoundsException e) {

										}
									}
								}
							}

							set.disconnect();
							fi.close();
						}

						System.out.println();
						System.out.println("wrongList list: ");
						for (String element : wrongList) {
							System.out.print(element + " ");
						}
						System.out.println();
						q.close();
						fo.close();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println();
						System.out.println(code + " may be wrong");
						wrongList.add(code);
						file.delete();
					} finally {
					}
				}
			}
			for (String element : wrongList) {
				System.out.print(element + " ");
			}
			System.out.println();
		}
		System.out.println("\nover!");
	}

	public static void writer(BufferedWriter q, String input) {
		input = input.substring(0, input.length() - 5);
		input += "</td><td>";
		String inputArray[] = input.split("</td><td>");
		// System.out.println(inputArray[0]);
		try {
			for (int i = 0; i < inputArray.length; i++) {
				if (i == 0) {
					String elementString = inputArray[0];
					elementString = turnToYear(elementString);
					q.write(elementString + ",");
				} else {
					String elementString = subComma(inputArray[i]);
					if (i != inputArray.length - 1) {
						q.write(elementString + ",");
					} else {
						q.write(elementString);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("alert");
		}
	}

	public static String subComma(String input) {
		if (input.contains(",")) {
			String inputArray[] = input.split(",");
			input = "";
			for (String elementString : inputArray) {
				input += elementString;
			}
		}
		return input;
	}

	public static String turnToYear(String input) {
		String output = "";
		char elementArray[] = input.toCharArray();
		input = "";
		for (char element : elementArray) {
			if (Character.isDigit(element))
				input += String.valueOf(element);
			else
				input += "/";
			output = input.substring(0, input.length() - 1);
		}
		return output;
	}

	public static int findRecordNumber(String code) {
		int result = 0;
		try {
			URL url = new URL("http://info.finance.yahoo.co.jp/history/?code="
					+ code
					+ ".T&sy=1983&sm=1&sd=1&ey=2013&em=12&ed=31&tm=d&p=1");
			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			set.setRequestProperty("Accept-Language", "jp");
			set.connect();
			BufferedReader fi = new BufferedReader(new InputStreamReader(
					set.getInputStream()));
			String input;

			while ((input = fi.readLine()) != null) {
				if (input.contains("stocksHistoryPageing")) {
					int endpoint = input.indexOf("件中");
					int startpoint = input.indexOf("件");
					result = Integer.valueOf(input.substring(startpoint + 2,
							endpoint));
					break;
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> readcode(String address) {
		ArrayList<String> code = new ArrayList<>();
		try {
			FileReader fr = new FileReader(address);
			BufferedReader br = new BufferedReader(fr);
			String nextline;
			// for(int i = 0; i<1670;i++)
			// code 9632
			br.readLine();
			while ((nextline = br.readLine()) != null) {
				// System.out.println(nextline);
				String element[] = nextline.split(",");
				code.add(element[1]);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static int getallnumber(String address) {
		int result = 0;
		try {
			FileReader fr = new FileReader(address);
			BufferedReader br = new BufferedReader(fr);
			String nextline = "";
			br.readLine();
			while ((nextline = br.readLine()) != null) {
				result++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean ifOutputAlready(String code, String address) {
		boolean result = false;
		ArrayList<String> outputtedFiles = getOutputtedFiles(address);
		for (String filePath : outputtedFiles) {
			if (filePath.contains(code)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static ArrayList<String> getOutputtedFiles(String address) {
		ArrayList<String> result = new ArrayList<>();
		File f = new File(address);
		/*
		 * try { Desktop.getDesktop().open(f); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		File[] list = f.listFiles();
		for (int i = 0; i < list.length; i++)
			result.add(list[i].getAbsolutePath());
		return result;
	}

	public static ArrayList<String> getInputtedFiles(String address) {
		ArrayList<String> result = new ArrayList<>();
		File f = new File(address);
		/*
		 * try { Desktop.getDesktop().open(f); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		File[] list = f.listFiles();
		for (int i = 0; i < list.length; i++)
			result.add(list[i].getAbsolutePath());
		return result;
	}
}
