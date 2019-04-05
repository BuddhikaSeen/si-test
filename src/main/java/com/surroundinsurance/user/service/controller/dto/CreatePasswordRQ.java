package com.surroundinsurance.user.service.controller.dto;

public class CreatePasswordRQ {

	private String code;
	
	private String password;
	
	public CreatePasswordRQ(){
		
	}

	public CreatePasswordRQ(String code, String password) {
		super();
		this.code = code;
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public String getPassword() {
		return password;
	}
	
}
