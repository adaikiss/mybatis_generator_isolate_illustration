/**
 * 
 */
package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hlw
 * 
 */
public class SqlUtil {

	private static final Logger logger = LoggerFactory.getLogger(SqlUtil.class);

	@SuppressWarnings("unchecked")
	public static void runScript(String url, String user, String password, String driverClassName, String... sqlFiles)
			throws SQLException, IOException, ClassNotFoundException {
		Class<? extends Driver> driver = (Class<Driver>) Class.forName(driverClassName);
		Connection conn = getConnection(url, user, password, driver);
		for (String sqlFile : sqlFiles) {
			logger.debug("Running script {}", sqlFile);
			runScript(conn, sqlFile);
		}
		conn.close();
	}

	public static void runScript(Connection conn, String sqlFile) throws IOException {
		File file = Resources.getResourceAsFile(sqlFile);
		ScriptRunner runner = new ScriptRunner(conn);
		runner.runScript(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")));
	}

	public static Connection getConnection(String url, String user, String password, Class<? extends Driver> driver)
			throws ClassNotFoundException, SQLException {
		Class.forName(driver.getName());
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		runScript("jdbc:mysql://localhost:3306/mybatis", "root", "", "org.h2.Driver", "database/schema.sql");
	}

}
