/**
 * 
 */
package com.example.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.mybatis.IsolateSqlSessionFactoryBuilder;

/**
 * @author hlw
 * 
 */
public class MyBatisUtil {
	private static final Logger logger = LoggerFactory.getLogger(MyBatisUtil.class);
	private static SqlSessionFactory sqlSessionFactory;
	public static final String MYBATIS_CONFIG_FILE = "mybatis-config.xml";

	private static boolean inited = false;

	private MyBatisUtil() {
	}

	public static synchronized void init() {
		InputStream configIs = null;
		try {
			configIs = Resources.getResourceAsStream(MYBATIS_CONFIG_FILE);
			sqlSessionFactory = new IsolateSqlSessionFactoryBuilder().build(configIs, Config.getInstance().getProps());
			inited = true;
		} catch (IOException e) {
			logger.error("Initial SessionFactory creation failed.", e);
			throw new ExceptionInInitializerError(e);
		} finally {
			if (configIs != null) {
				try {
					configIs.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		if (inited) {
			return sqlSessionFactory;
		}
		throw new RuntimeException("not yet initialized!");
	}
}
