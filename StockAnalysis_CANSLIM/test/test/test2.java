package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DBNewYorkDao;
import dao.DBSydenyDao;
import datasource.DataSourceUtil;

public class test2 {

	public static void main(String args[]) {
		Connection con = null;
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			ArrayList<String> list = DBNewYorkDao.getCodeListFromDB(con);
			for (String code : list) {
				if (code.contains("/")) {
					System.out.println(code);
				}
			}
			con = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
