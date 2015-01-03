/**
 * 
 */
package com.example.base.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.example.base.domain.BaseExample;
import com.example.base.mapper.BaseMapper;
import com.example.page.Page;
import com.example.page.PageRequest;
import com.example.page.PageRequest.Sort;

/**
 * @author hlw
 * 
 */
public abstract class BaseService<T> {
	public List<T> findAll(BaseExample<T> example) {
		return getMapper().selectByExample(example);
	}

	public int countAll(BaseExample<T> example) {
		return getMapper().countByExample(example);
	}

	public List<T> findAll(BaseExample<T> example, int offset, int limit) {
		return getMapper().selectByExample(example, new RowBounds(offset, limit));
	}

	public Page<T> findAll(BaseExample<T> example, PageRequest page) {
		int total = getMapper().countByExample(example);
		if (total == 0) {
			return new Page<T>(page);
		}
		page.confirm(total);
		if (example.getOrderByClause() != null && page.hasOrderByClause()) {
			StringBuilder sb = new StringBuilder().append(example.getOrderByClause());
			for (Sort s : page.getSort()) {
				sb.append(", ");
				sb.append(s.getProperty()).append(" ").append(s.getOrder());
			}
			example.setOrderByClause(sb.toString());
		} else {
			example.setOrderByClause(page.getOrderByClause());
		}
		return new Page<T>(getMapper().selectByExample(example, getRowbounds(page)), page, total);
	}

	protected RowBounds getRowbounds(PageRequest page) {
		return new RowBounds(page.getOffset(), page.getPageSize());
	}

	protected abstract BaseMapper<T> getMapper();
}
