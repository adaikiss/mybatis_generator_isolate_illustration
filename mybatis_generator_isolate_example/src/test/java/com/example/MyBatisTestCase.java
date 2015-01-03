/**
 * 
 */
package com.example;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.example.utils.Config;
import com.example.utils.MyBatisUtil;
import com.example.utils.SqlUtil;

/**
 * @author hlw
 * 
 */
public class MyBatisTestCase {
	protected SqlSessionFactory sqlSessionFactory;
	protected static String schemaSql = "database/schema.sql";
	protected static String dataSql = "database/data.sql";

	@BeforeClass
	public static void setUp() throws Exception {
		Config config = Config.getInstance();
		SqlUtil.runScript(config.getUrl(), config.getUsername(), config.getPassword(), config.getDriver(), schemaSql, dataSql);
		MyBatisUtil.init();
	}

	@Before
	public void before() throws Exception{
		sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
	}

	@After
	public void tearDown() throws Exception {

	}

}
