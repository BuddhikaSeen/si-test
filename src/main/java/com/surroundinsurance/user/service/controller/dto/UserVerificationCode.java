package com.surroundinsurance.user.service.controller.dto;

public class UserVerificationCode {

	private String verificationCode;
	
	private String oneTimePassword;

	public UserVerificationCode(String verificationCode, String oneTimePassword) {
		super();
		this.verificationCode = verificationCode;
		this.oneTimePassword = oneTimePassword;
	}

	public UserVerificationCode(String verificationCode) {
		super();
		this.verificationCode = verificationCode;
	}

	public String getVerficationCode() {
		return verificationCode;
	}

	public String getOneTimePassword() {
		return oneTimePassword;
	}
	
}
