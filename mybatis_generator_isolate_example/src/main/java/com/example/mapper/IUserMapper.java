/**
 * 
 */
package com.example.mapper;

import com.example.domain.User;

/**
 * @author hlw
 *
 */
public interface IUserMapper {
	public User selectByPrimaryKeyWithRoles(Long id);
}
