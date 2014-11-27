/**
 * 
 */
package com.example.mapper;

import java.util.List;

import com.example.domain.Permission;

/**
 * @author hlw
 *
 */
public interface IPermissionMapper {
	List<Permission> selectByRoleId(Long id);
}
