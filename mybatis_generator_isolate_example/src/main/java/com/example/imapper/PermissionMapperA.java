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
public interface PermissionMapperA {
	List<Permission> selectByRoleId(Long id);
}
