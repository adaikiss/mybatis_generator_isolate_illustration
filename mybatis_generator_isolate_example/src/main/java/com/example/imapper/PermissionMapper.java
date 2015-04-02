/**
 * 
 */
package com.example.imapper;

import java.util.List;

import com.example.domain.Permission;

/**
 * @author hlw
 *
 */
public interface PermissionMapper {
	List<Permission> selectByRoleId(Long id);
}
