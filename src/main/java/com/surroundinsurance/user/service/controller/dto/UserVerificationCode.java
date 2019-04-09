package com.surroundinsurance.user.service.controller.dto;

public class UserVerificationCode {

	private String verficationCode;
	
	private String oneTimePassword;

	public UserVerificationCode(String verficationCode, String oneTimePassword) {
		super();
		this.verficationCode = verficationCode;
		this.oneTimePassword = oneTimePassword;
	}

	public UserVerificationCode(String verficationCode) {
		super();
		this.verficationCode = verficationCode;
	}

	public String getVerficationCode() {
		return verficationCode;
	}

	public String getOneTimePassword() {
		return oneTimePassword;
	}
	
}
