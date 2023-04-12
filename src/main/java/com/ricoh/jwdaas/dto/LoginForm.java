package com.ricoh.jwdaas.dto;

/** 
  * @Title: LoginForm.java 
  * @Copyright: Copyright (c) 2023 
  * @Company: RICOH
  * @version 1.0  
  * @author jwChen  
  * @date 2023年3月31日  
  */

public class LoginForm {

	private String username;
	private String password;
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
	@Override
	public String toString() {
		return "LoginForm [username=" + username + ", password=" + password + "]";
	}
	
}
