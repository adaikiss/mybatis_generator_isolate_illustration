/**
 * 
 */
package com.example;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;

import com.example.utils.Config;
import com.example.utils.MyBatisUtil;
import com.example.utils.SqlUtil;

/**
 * @author hlw
 * 
 */
public class MyBatisTestCase {
	protected SqlSessionFactory sqlSessionFactory;
	protected String schemaSql = "database/schema.sql";
	protected String dataSql = "database/data.sql";

	@Before
	public void setUp() throws Exception {
		Config config = Config.getInstance();
		SqlUtil.runScript(config.getUrl(), config.getUsername(), config.getPassword(), config.getDriver(), schemaSql, dataSql);
		MyBatisUtil.init();
		sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
	}

	@After
	public void tearDown() throws Exception {

	}

}
