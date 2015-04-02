/**
 * 
 */
package com.example.mybatis;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;

/**
 * mysql pagination sql source append offset/limit to sql
 * 
 * @author hlw
 * 
 */
public class PaginationSqlSource implements SqlSource {
	public static ThreadLocal<RowBounds> _rowBounds = new ThreadLocal<RowBounds>();
	private SqlSource source;
	private Field sqlField;

	public PaginationSqlSource(SqlSource source) {
		this.source = source;
		try {
			this.sqlField = BoundSql.class.getDeclaredField("sql");
			sqlField.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		return buildPaginationBoundSql(this.source.getBoundSql(parameterObject));
	}

	private BoundSql buildPaginationBoundSql(BoundSql boundSql) {
		RowBounds rowBounds = _rowBounds.get();
		if (null == rowBounds || rowBounds == RowBounds.DEFAULT) {
			return boundSql;
		}
		try {
            //mysql
			sqlField.set(boundSql,
					new StringBuilder().append(sqlField.get(boundSql)).append(" LIMIT ").append(rowBounds.getLimit())
							.append(" OFFSET ").append(rowBounds.getOffset()).toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return boundSql;
	}
}
