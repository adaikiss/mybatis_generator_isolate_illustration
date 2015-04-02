/**
 * 
 */
package com.example.idomain;

import com.example.domain.Role;

import java.util.List;

/**
 * @author hlw
 * 
 */
public abstract class User {
	protected List<com.example.domain.Role> roles;

	public List<com.example.domain.Role> getRoles() {
		return roles;
	}

	public void setRoles(List<com.example.domain.Role> roles) {
		this.roles = roles;
	}

}
