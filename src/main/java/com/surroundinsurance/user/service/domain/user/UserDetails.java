package com.surroundinsurance.user.service.domain.user;

public class UserDetails {

	private User user;
	
    private boolean newUser;
    
    // Temporary property to send verification code since email is not configured
    private String verificationCode;
    
    private String oneTimePassword;

	public UserDetails(User user, boolean newUser) {
		super();
		this.user = user;
		this.newUser = newUser;
	}

	public UserDetails(User user, boolean newUser, String verificationCode, String oneTimePassword) {
		super();
		this.user = user;
		this.newUser = newUser;
		this.verificationCode = verificationCode;
		this.oneTimePassword = oneTimePassword;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public User getUser() {
		return user;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public String getOneTimePassword() {
		return oneTimePassword;
	}

}
