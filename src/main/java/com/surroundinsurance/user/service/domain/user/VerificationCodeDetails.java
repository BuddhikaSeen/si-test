package com.surroundinsurance.user.service.domain.user;

public class VerificationCodeDetails {

	private VerificationCode verificationCode;
	
    private VerificationCode oneTimePassword;

	public VerificationCodeDetails(VerificationCode verificationCode, VerificationCode oneTimePassword) {
		super();
		this.verificationCode = verificationCode;
		this.oneTimePassword = oneTimePassword;
	}

	public VerificationCode getVerificationCode() {
		return verificationCode;
	}

	public VerificationCode getOneTimePassword() {
		return oneTimePassword;
	}

}
