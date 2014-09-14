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
		String input = "</td></tr><tr><td colspan=\"2\">Cost of Revenue</td><td align=\"right\">"
				+ "307,625&nbsp;&nbsp;</td><td align=\"right\">"
				+ "237,413&nbsp;&nbsp;</td></tr><tr><td colspan=\"4\"  style=\"height:0;padding:0; "
				+ "border-top:3px solid #333;\"><span style=\"display:block; width:5px; height:1px;\" >"
				+ "</span></td></tr><tr><td colspan=\"2\">";
		String mark = "</td><td align=\"right\">";
		System.out.println(charDeal.countMark(input, mark));
		
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
