/**
 * 
 */
package com.example.imapper;

import com.example.domain.User;

/**
 * @author hlw
 *
 */
public interface UserMapperA {
	public User selectByPrimaryKeyWithRoles(Long id);
}
