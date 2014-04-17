package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import datasource.DataSourceUtil;

public class CreateSectionTableFromCsv {

	private final String sqlDelete = "truncate Section_Tokyo";
	private final String sqlDrop = "DROP TABLE Section_Tokyo";
	private final String sqlCreateTable = "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;"
			+ "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;"
			+ "SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';"
			+ "CREATE SCHEMA IF NOT EXISTS `TokyoStockExchange` DEFAULT CHARACTER SET utf8 ;"
			+ "USE `TokyoStockExchange` ;"
			+ "-- -----------------------------------------------------"
			+ "-- Table `TokyoStockExchange`.`Section_Tokyo`"
			+ "-- -----------------------------------------------------"
			+ "CREATE TABLE IF NOT EXISTS `TokyoStockExchange`.`Section_Tokyo` ("
			+ "`Effective_Date` DATE NOT NULL,"
			+ "`Country` VARCHAR(50) NOT NULL,"
			+ "`Department` VARCHAR(100) NOT NULL,"
			+ "`Local_Code` INT NOT NULL,"
			+ "`Name_English` VARCHAR(100) NOT NULL,"
			+ "`33_Sector_code` INT NOT NULL,"
			+ "`33_Sector_name` VARCHAR(100) NOT NULL,"
			+ "`17_Sector_code` INT NOT NULL,"
			+ "`17_Sector_name` VARCHAR(100) NOT NULL,"
			+ "`Size_Code_New_Index_Series` INT NOT NULL,"
			+ "`Size_New_Index_Series` VARCHAR(100) NOT NULL,"
			+ "PRIMARY KEY (`Local_Code`, `Name_English`))"
			+ "ENGINE = InnoDB;"
			+ "SET SQL_MODE=@OLD_SQL_MODE;"
			+ "SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;"
			+ "SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;";
	private final String sqlInsert = 
			"LOAD DATA LOCAL INFILE ? INTO TABLE Section_Tokyo "
			+ "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' "
			+ "LINES TERMINATED BY '\n' STARTING BY '' "
			+ "IGNORE ? LINES "
			+ "(Effective_Date,Country,Department,Local_Code,Name_English,"
			+ "33_Sector_code,33_Sector_name,17_Sector_code,17_Sector_name,"
			+ "Size_Code_New_Index_Series,Size_New_Index_Series)";

	public CreateSectionTableFromCsv() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		
		String address = "/Users/Daytona/Documents/workspace/StockAnalyzeJapan/RawData/TokyoSectionCategory.csv";
		Connection con = null;
		CreateSectionTableFromCsv self = new CreateSectionTableFromCsv();
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			PreparedStatement stmt = con.prepareStatement(self.getSqlDelete());
			stmt.execute();
			System.out.println("deleted original data");
			System.out.println(self.getSqlInsert());

			stmt = con.prepareStatement(self.getSqlInsert());
			stmt.setString(1, address);
			stmt.setInt(2, 0);
			stmt.execute();
			System.out.println("insert completed");

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
	}

	public String getSqlDelete() {
		return sqlDelete;
	}

	public String getSqlInsert() {
		return sqlInsert;
	}

	public String getSqlCreateTable() {
		return sqlCreateTable;
	}

	public String getSqlDrop() {
		return sqlDrop;
	}
	
	

}
