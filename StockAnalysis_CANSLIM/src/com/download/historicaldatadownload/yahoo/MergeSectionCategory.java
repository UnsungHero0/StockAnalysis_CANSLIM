package com.download.historicaldatadownload.yahoo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MergeSectionCategory {

	public MergeSectionCategory() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		// TODO
		// collect file name
		ArrayList<File> fileNameList = new ArrayList<>();
		String address = "/Users/Daytona/Documents/workspace/StockAnalyzeJapan/RawData";
		fileNameList = getFileNameList(address);
		// collect all the content
		ArrayList<ArrayList<String>> container = new ArrayList<>();
		container = getAllFileContent(fileNameList);

		//output to csv
		writeIntoDatabase(container);
	}

	public static ArrayList<File> getFileNameList(String address) {
		ArrayList<File> fileLists = new ArrayList<>();
		File folder = new File(address);
		File[] fileList = folder.listFiles();
		for (File file : fileList) {
			if (file.getName().contains("csv")) {
				fileLists.add(file);
			}
		}
		return fileLists;
	}

	public static ArrayList<ArrayList<String>> getAllFileContent(
			ArrayList<File> fileNameList) {
		// TODO
		ArrayList<ArrayList<String>> container = new ArrayList<>();
		try {
			BufferedReader br = null;
			for (File filePath : fileNameList) {
				FileReader file = new FileReader(filePath);
				br = new BufferedReader(file);
				br.readLine();
				String line = null;
				
				while((line = br.readLine())!=null) {
					ArrayList<String> element = new ArrayList<>();
					String content[] = line.split(",");
					for (int i = 0; i < content.length; i ++) {
						if (i == 1) {
							element.add("Tokyo");
							String fileNameString = filePath.getName().split("\\.")[0].replace(" ", "");
							element.add(fileNameString);
							element.add(content[1]);
						} else {
							element.add(content[i]);
						}
					}
					container.add(element);
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
		Collections.sort(container, 
				new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(3).compareTo(o2.get(3));
			}
		});
		Collections.sort(container, 
				new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(2).compareTo(o2.get(2));
			}
		});
		return container;
	}
	
	public static void writeIntoDatabase(ArrayList<ArrayList<String>> container) {
		// TODO
		
		BufferedWriter bw = null;
		try {
			File file = new File("/Users/Daytona/Documents/workspace/StockAnalyzeJapan/RawData/TokyoSectionCategory.csv");
			if (!file.exists()){
			FileWriter fw = new FileWriter(new File("/Users/Daytona/Documents/workspace/StockAnalyzeJapan/RawData/TokyoSectionCategory.csv"));
			bw = new BufferedWriter(fw);
			for (ArrayList<String> line : container) {
				for (int i = 0; i < line.size(); i ++) {
					bw.write(line.get(i) + ",");
				}
				bw.newLine();
			}
			}
		} catch (IOException e) {
			
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

}
