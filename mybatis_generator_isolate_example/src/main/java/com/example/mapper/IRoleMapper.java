/**
 * 
 */
package com.example.mapper;

import java.util.List;

import com.example.domain.Role;

/**
 * @author hlw
 * 
 */
public interface IRoleMapper {
	List<Role> selectByUserId(Long id);

	List<Role> selectByUserIdWithPerms(Long id);
}
