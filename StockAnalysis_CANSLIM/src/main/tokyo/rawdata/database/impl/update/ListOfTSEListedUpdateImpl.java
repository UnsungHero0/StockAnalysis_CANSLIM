package impl.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import datasource.DataSourceUtil;
import module.ListOfTSEListed.ListOfTSEListedUpdate;
import namespace.DBNameSpace;

public class ListOfTSEListedUpdateImpl {
	
	public ListOfTSEListedUpdateImpl() {
	}

	public static void main(String args[]) {

		
		
		
		run();
		

	}

	public static void run() {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println(DBNameSpace.getListedcompaniesTokyoDb()
				+ " download is started!");
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			ListOfTSEListedUpdate.updataListedCompanyList(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;

		System.out.println(DBNameSpace.getListedcompaniesTokyoDb()
				+ " update is finished!");
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
	}

}
