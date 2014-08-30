package jdbcdao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import datasource.DataSourceUtil;


public class CodeListsDao {

	public CodeListsDao() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<String> getCodeLists(DataSource dataSource) {
		//TODO
		ArrayList<String> codeList = new ArrayList<>();
		String selectCodeListsSql = "SELECT Local_Code FROM listedcompaniestokyo";
		Connection con = null;
		try {
			con = dataSource.getConnection();
			ResultSet rs = con.prepareStatement(selectCodeListsSql).executeQuery();
			while (rs.next()) {
				codeList.add(rs.getString("Local_Code"));
			}
			con.close();
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
		return codeList;
	}
	
	public ArrayList<String> getCodeListsFromFinancialStatement() {
		//TODO
		ArrayList<String> codeList = new ArrayList<>();
		String selectCodeListsSql = "SELECT DISTINCT Local_Code FROM FinancialStatementTokyo_test";
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ResultSet rs = con.prepareStatement(selectCodeListsSql).executeQuery();
			while (rs.next()) {
				codeList.add(rs.getString("Local_Code"));
			}
			con.close();
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
		return codeList;
	}

}
