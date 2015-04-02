/**
 * 
 */
package com.example.idomain;

import com.example.domain.Permission;

import java.util.List;

/**
 * @author hlw
 *
 */
public class Role {
	protected List<Permission> perms;

	public List<Permission> getPerms() {
		return perms;
	}

	public void setPerms(List<Permission> perms) {
		this.perms = perms;
	}
}
