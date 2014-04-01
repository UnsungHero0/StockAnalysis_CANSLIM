package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;
import com.download.historicaldatadownload.yahoo.jdbc.dao.CodeListsDao;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) {
		/*
		Connection con = null;
		try {
			String selectEnglishNameSql = "DELETE FROM `TokyoStockExchange_test`.`Section_Tokyo` WHERE "
					+ "Local_Code = 2372";
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			con.prepareStatement(selectEnglishNameSql)
					.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		*/
		/*
		Connection con = null;
		try {
			String selectEPSRecord = "SELECT Fiscal_Year, EPS FROM FinancialStatementTokyo_test WHERE "
					+ "Local_Code = 1334 AND Form = "
							+ "'independent' ORDER BY Fiscal_Year";
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ResultSet rs = con.prepareStatement(selectEPSRecord).executeQuery();
			while(rs.next()) {
				System.out.println(rs.getFloat("EPS"));
				//if (rs.getFloat("EPS") > 0)
				//result.add(rs.getFloat("EPS"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con!=null) {
				try{
				con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		*/
		Connection con = null;
		CodeListsDao cld = new CodeListsDao();
		ArrayList<String> codeList = cld.getCodeListsFromFinancialStatement();
		String codeListString = "";
		for (String code : codeList) {
			codeListString = codeListString + code + ", ";
		}
		codeListString = "("+ codeListString.substring(0,codeListString.length()-2)+ ")";
		System.out.println(codeListString);
		/*
		try {
			String selectEPSRecord = "SELECT Distinct Department FROM Section_Tokyo WHERE Local_Code IN "
					+ "()";
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ResultSet rs = con.prepareStatement(selectEPSRecord).executeQuery();
			while(rs.next()) {
				System.out.println(rs.getFloat("EPS"));
				//if (rs.getFloat("EPS") > 0)
				//result.add(rs.getFloat("EPS"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con!=null) {
				try{
				con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}*/
	}
	
	
	public String getEnglishName(String code) {
		String name = null;
		Connection con = null;
		try {
			String selectEnglishNameSql = "SELECT Name_English FROM `TokyoStockExchange_test`.`Section_Tokyo` WHERE "
					+ "Local_code = " + code;
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ResultSet rs = con.prepareStatement(selectEnglishNameSql)
					.executeQuery();
			rs.next();
			name = rs.getString("Name_English");
			if(name.contains("'")) {
				String nameArray[] = name.split("'");
				name = "";
				for (int i = 0; i < nameArray.length; i ++) {
					if (i!=0) {
					name = name + "\\'" + nameArray[i];
					} else {
						name =  name + nameArray[i];
					}
				}
				System.out.println(name);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return name;
	}

}
