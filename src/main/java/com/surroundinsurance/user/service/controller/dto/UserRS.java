package com.surroundinsurance.user.service.controller.dto;

import io.swagger.annotations.ApiModel;

/**
 * The Class User.
 */
@ApiModel(value = "UserRS")
public class UserRS {

	/** The id. */
	private String id;
	
	private UserStateType userStateType;
	
	private boolean newUser;
	
	private boolean verified;
	
	private String verificationCode;

	/**
	 * Instantiates a new user rs.
	 */
	public UserRS() {

	}

	/**
	 * Instantiates a new user rs.
	 *
	 * @param id            the id
	 */
	public UserRS(String id) {
		super();
		this.id = id;
	}
	
	/**
	 * Instantiates a new user rs.
	 *
	 * @param id            	the id
	 * @param userStateType     the user state type
	 */
	public UserRS(String id, UserStateType userStateType) {
		super();
		this.id = id;
		this.userStateType = userStateType;
	}

	public UserRS(String id, UserStateType userStateType, boolean newUser, boolean verified, String verificationCode) {
		super();
		this.id = id;
		this.userStateType = userStateType;
		this.newUser = newUser;
		this.verified = verified;
		this.verificationCode = verificationCode;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the user state type.
	 *
	 * @return the user state type
	 */
	public UserStateType getUserStateType() {
		return userStateType;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public boolean isVerified() {
		return verified;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

}
