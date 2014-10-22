package test;
// to import shanghai future exchange data into database from excel

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import tool.charDeal;
import jxl.Sheet;
import jxl.Workbook;

public class test4 {

	public static void main(String args[]) {

		try {
			String years[] = {"2011","2012","2013","2014"};
			ArrayList<ArrayList<String>> input = new ArrayList<>();
			for (String str : years) {
			File sourcefile = new File(
					"E:\\Dropbox\\trade\\HistoricalData\\ShanghaiFutureExchnage\\MarketData\\"+str+"price.xls");
			InputStream is = new FileInputStream(sourcefile);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			Sheet rs = rwb.getSheet(0);
			String item = "";
			
			
			for (int j = 3; j < rs.getRows()-6; j++) {
				ArrayList<String> oneInput = new ArrayList<>();
				for (int i = 0; i < rs.getColumns()-1; i++) {
					if (i == 0 && j == 3) {
						item = rs.getCell(i, j).getContents();
						oneInput.add(item);
						//System.out.print(item + "/");
					} else if ((i == 0 && j > 3) && !rs.getCell(i, j).getContents().equals("")) {
						item = rs.getCell(i, j).getContents();
						oneInput.add(item);
						//System.out.print(item + "/");
					} else if ((i == 0 && j > 3) && rs.getCell(i, j).getContents().equals("")) {
						oneInput.add(item);
						//System.out.print(item + "/");
					} else {
					String strc = charDeal.subComma(rs.getCell(i, j).getContents());
					oneInput.add(strc);
					//System.out.print(strc + "/");
					}
				}
				input.add(oneInput);
				//System.out.println();
			}
			}
			for (ArrayList<String> ele : input) {
				for (String str : ele) {
					System.out.print(str+",");
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
