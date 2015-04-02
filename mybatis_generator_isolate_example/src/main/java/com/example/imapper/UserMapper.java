/**
 * 
 */
package com.example.imapper;

import com.example.domain.User;

/**
 * @author hlw
 *
 */
public interface UserMapper {
	public User selectByPrimaryKeyWithRoles(Long id);
}
