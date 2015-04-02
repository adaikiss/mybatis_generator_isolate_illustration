/**
 * 
 */
package com.example.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;

/**
 * @author hlw
 *
 */
public class Config {
	private static Config instance;
	private String username;
	private String password;
	private String driver;
	private String url;
	private Properties props;
	private Config(){
		load();
	}

	private void load(){
		try {
			props = Resources.getResourceAsProperties("db.properties");
			this.username = props.getProperty("db.username");
			this.password = props.getProperty("db.password");
			this.driver = props.getProperty("db.driver");
			this.url = props.getProperty("db.url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final Config getInstance(){
		if(null == instance){
			instance = new Config();
		}
		return instance;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}
}
