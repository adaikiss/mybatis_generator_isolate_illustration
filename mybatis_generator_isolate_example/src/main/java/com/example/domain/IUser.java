/**
 * 
 */
package com.example.domain;

import java.util.List;

/**
 * @author hlw
 * 
 */
public abstract class IUser {
	protected List<Role> roles;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
