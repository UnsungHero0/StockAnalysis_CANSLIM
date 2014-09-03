package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DBSydenyDao;
import datasource.DataSourceUtil;

public class test2 {
	
	public static void main(String args[]) {
		Connection con = null;
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			String result = DBSydenyDao.getNameEnglish("3PL", con);
				System.out.println(result);
			con = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
