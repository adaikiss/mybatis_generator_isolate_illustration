/**
 * 
 */
package com.example.base.domain;

/**
 * @author hlw
 *
 */
public abstract class BaseExample<T> {
	public abstract void setOrderByClause(String orderByClause);

	public abstract String getOrderByClause();
}
