package com.download.historicaldatadownload.yahoo;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.TrustAnchor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import com.analysis.date.DateOperation;
import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class test1 {

	public static void main(String args[]) {
		
		Date date = new Date();
	
		String code = "1301";
		try {
			Connection con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			String selectLatestDaySql = "SELECT MIN(Date) Date FROM "
					+ "?_HistoricalQuotes_Tokyo";
			PreparedStatement stmt = con.prepareStatement(selectLatestDaySql);
			stmt.setInt(1, Integer.parseInt(code));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			date = rs.getDate("Date");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
		System.out.println(date);
		String dateNow = "1983-01-04";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateNoww = sdf.parse(dateNow);
			System.out.println(dateNoww.after(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		/*
		try {
			for (Integer i = 1; i < 50; i++) {
				URL url = new URL(
						"http://info.finance.yahoo.co.jp/history/?"
								+ "code=1301.T&sy=1983&sm=1&sd=1&ey=2013&em=12&ed=31&tm=d&p="
								+ i);
				HttpURLConnection set = (HttpURLConnection) url
						.openConnection();
				set.setRequestProperty("Accept-Language", "jp");
				set.connect();
				BufferedReader fi = null;
				fi = new BufferedReader(new InputStreamReader(
						set.getInputStream()));
				String input = "";
				
				while ((input = fi.readLine()) != null) {
					if (input.startsWith("</tr>")) {
						String inputRow[] = input
								.split("</tr><tr><td>");
						for (String row : inputRow) {
							System.out.println(row);
						}
					}
				}

			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

	}

	public static DataSource getDataSource() {
		String host = "127.0.0.1";
		Integer port = 3306;
		String dbname = "test";
		String username = "root";
		String password = "";
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setDatabaseName(dbname);
		dataSource.setUser(username);
		dataSource.setPassword(password);

		return dataSource;
	}

}
