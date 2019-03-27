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

}
