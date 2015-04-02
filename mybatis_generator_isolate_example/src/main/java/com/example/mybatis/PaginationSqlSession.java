/**
 * 
 */
package com.example.mybatis;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hlw
 * 
 */
public class PaginationSqlSession extends DefaultSqlSession {
	private Map<String, MappedStatement> mappedStatements = new HashMap<String, MappedStatement>();
	private Configuration configuration;
	private Executor executor;

	public PaginationSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
		super(configuration, executor, autoCommit);
		this.configuration = configuration;
		this.executor = executor;
	}

	public PaginationSqlSession(Configuration configuration, Executor executor) {
		this(configuration, executor, false);
	}

	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		try {
			MappedStatement ms = configuration.getMappedStatement(statement);
			// it's not the default rowBounds, there is a custom rowBounds
			if (rowBounds != RowBounds.DEFAULT) {
				MappedStatement rbms = mappedStatements.get(statement);
				if (null == rbms) {
					rbms = copyMappedStatement(ms);
					mappedStatements.put(statement, rbms);
				}
				ms = rbms;
				PaginationSqlSource._rowBounds.set(rowBounds);
				rowBounds = RowBounds.DEFAULT;
			}
			List<E> result = executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
			PaginationSqlSource._rowBounds.remove();
			return result;
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
		}
	}

	private Object wrapCollection(final Object object) {
		if (object instanceof List) {
			StrictMap<Object> map = new StrictMap<Object>();
			map.put("list", object);
			return map;
		} else if (object != null && object.getClass().isArray()) {
			StrictMap<Object> map = new StrictMap<Object>();
			map.put("array", object);
			return map;
		}
		return object;
	}

	private MappedStatement copyMappedStatement(MappedStatement ms) {
		MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(),
				new PaginationSqlSource(ms.getSqlSource()), ms.getSqlCommandType());
		statementBuilder.resource(ms.getResource());
		statementBuilder.fetchSize(ms.getFetchSize());
		statementBuilder.statementType(ms.getStatementType());
		statementBuilder.keyGenerator(ms.getKeyGenerator());
		String[] keyProperties = ms.getKeyProperties();
		statementBuilder.keyProperty(keyProperties == null ? null : keyProperties[0]);
		statementBuilder.keyColumn(join(ms.getKeyColumns(), ","));
		statementBuilder.databaseId(ms.getDatabaseId());
		statementBuilder.lang(ms.getLang());
		statementBuilder.resultOrdered(ms.isResultOrdered());
		statementBuilder.resulSets(join(ms.getResulSets(), ","));
		statementBuilder.timeout(ms.getTimeout());
		statementBuilder.parameterMap(ms.getParameterMap());
		statementBuilder.resultMaps(ms.getResultMaps());
		statementBuilder.resultSetType(ms.getResultSetType());
		statementBuilder.cache(ms.getCache());
		statementBuilder.flushCacheRequired(ms.isFlushCacheRequired());
		statementBuilder.useCache(ms.isUseCache());
		return statementBuilder.build();
	}

    private static String join(Object[] array, String separator){
        if(null == array || array.length == 0){
            return "";
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0;i < array.length;i++){
            if(i > 0){
                result.append(separator);
            }
            result.append(array[i]);
        }
        return result.toString();
    }
}
