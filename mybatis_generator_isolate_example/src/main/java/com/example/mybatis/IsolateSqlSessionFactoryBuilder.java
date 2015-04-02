/**
 * 
 */
package com.example.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * modified from {@link org.apache.ibatis.session.SqlSessionFactoryBuilder}
 * 
 * @author hlw
 * 
 */
public class IsolateSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {
	public SqlSessionFactory build(Reader reader) {
		return build(reader, null, null);
	}

	public SqlSessionFactory build(Reader reader, String environment) {
		return build(reader, environment, null);
	}

	public SqlSessionFactory build(Reader reader, Properties properties) {
		return build(reader, null, properties);
	}

	public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
		try {
			XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
			return build(parser.parse());
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
			ErrorContext.instance().reset();
			try {
				reader.close();
			} catch (IOException e) {
				// Intentionally ignore. Prefer previous error.
			}
		}
	}

	public SqlSessionFactory build(InputStream inputStream) {
		return build(inputStream, null, null);
	}

	public SqlSessionFactory build(InputStream inputStream, String environment) {
		return build(inputStream, environment, null);
	}

	public SqlSessionFactory build(InputStream inputStream, Properties properties) {
		return build(inputStream, null, properties);
	}

	public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
		try {
			XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
			Configuration configuration = parser.getConfiguration();
			// <!-- modified
			Field field = Configuration.class.getDeclaredField("mapperRegistry");
			field.setAccessible(true);
			field.set(configuration, new IsolateMapperRegistry(configuration));
			field.setAccessible(false);
			// -->
			return build(parser.parse());
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
			ErrorContext.instance().reset();
			try {
				inputStream.close();
			} catch (IOException e) {
				// Intentionally ignore. Prefer previous error.
			}
		}
	}

	public SqlSessionFactory build(Configuration config) {
		try {
			//modify mapperRegistry
			Field field = Configuration.class.getDeclaredField("mapperRegistry");
			field.setAccessible(true);
            Object oldMapperRegistry = field.get(config);
            //do not set twice!
            if(!IsolateMapperRegistry.class.isAssignableFrom(oldMapperRegistry.getClass())){
                field.set(config, new IsolateMapperRegistry(config));
            }
			field.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// -->
        //use PaginationSqlSessionFactory(for PaginationSqlSession) to handle rowBounds
		return new PaginationSqlSessionFactory(config);
	}

    /**
     *
     * @param str isolateReplaceSource|isolateReplaceDest
     */
	public void setIsolateReplacement(String str){
		if(StringUtils.isBlank(str)){
			return;
		}
		String[] tuple = str.split("\\|");
		if(tuple.length != 2){
			throw new RuntimeException("isolateReplacement property should be two string concated with |");
		}
		IsolateXMLMapperBuilder.isolateReplaceSource = tuple[0];
		IsolateXMLMapperBuilder.isolateReplaceDest = tuple[1];
	}
}
