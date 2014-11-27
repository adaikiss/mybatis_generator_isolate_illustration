/**
 * 
 */
package com.example.domain;

import java.util.List;

/**
 * @author hlw
 *
 */
public class IRole {
	protected List<Permission> perms;

	public List<Permission> getPerms() {
		return perms;
	}

	public void setPerms(List<Permission> perms) {
		this.perms = perms;
	}
}
