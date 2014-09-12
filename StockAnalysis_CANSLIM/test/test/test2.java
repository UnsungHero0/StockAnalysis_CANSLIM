package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import tool.charDeal;
import commontool.JDBCUtil;
import dao.DBNewYorkDao;
import dao.DBSydenyDao;
import datasource.DataSourceUtil;

public class test2 {

	public static void main(String args[]) {
		
		System.out.println(charDeal.extractDigital("(gfdg3ff4dad25"));
		
		/*
		Connection con = null;
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			System.out.println(JDBCUtil.hasTable("newyorkexchange.a_historicalquotes_newyork", con));
			String li = "-";
			System.out.println(li.equals("-"));
			
			con = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
}
