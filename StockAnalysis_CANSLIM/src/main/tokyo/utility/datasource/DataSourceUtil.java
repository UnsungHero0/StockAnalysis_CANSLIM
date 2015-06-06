package datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceUtil {

	public DataSourceUtil() {
	}

	public static DataSource getTokyoDataSourceRoot(){
		String host = "127.0.0.1";
		Integer port = 3306;
		String dbname = "TokyoStockExchange_test";
		String username = "root";
		String password = "Ding198573jie";
		MysqlDataSource dataSource = new MysqlDataSource();
		
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setDatabaseName(dbname);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		try {
			dataSource.setLoginTimeout(600);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSource;
	}
	
	public static DataSource DINGUNSW(){
		String host = "149.171.37.73";
		Integer port = 3306;
		String dbname = "tokyoexchange";
		String username = "root";
		String password = "4573";
		MysqlDataSource dataSource = new MysqlDataSource();
		
		dataSource.setServerName(host);
		dataSource.setPort(port);
		dataSource.setDatabaseName(dbname);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		try {
			dataSource.setLoginTimeout(600);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSource;
	}

}
