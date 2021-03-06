/**
 * 
 */
package com.example.imapper;

import java.util.List;

import com.example.domain.Role;

/**
 * @author hlw
 * 
 */
public interface RoleMapper {
	List<Role> selectByUserId(Long id);

	List<Role> selectByUserIdWithPerms(Long id);
}
