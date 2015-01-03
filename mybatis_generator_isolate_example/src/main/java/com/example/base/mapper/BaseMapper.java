/**
 * 
 */
package com.example.base.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.example.base.domain.BaseExample;

/**
 * @author hlw
 *
 */
public interface BaseMapper<T> {
	int countByExample(BaseExample<T> example);

	List<T> selectByExample(BaseExample<T> example, RowBounds rowBounds);

	List<T> selectByExample(BaseExample<T> example);
}
