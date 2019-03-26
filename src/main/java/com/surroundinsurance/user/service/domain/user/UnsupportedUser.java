package com.surroundinsurance.user.service.domain.user;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Class User.
 */
@Document(collection = "unsupported_user")
public class UnsupportedUser {

	/** The id. */
	@Id
	private String id;

	/** The partner id. */
	private String partnerId;

	/** The email. */
	@Indexed
	private String email;
	
	/** The state. */
	private String state;

	/** The created date. */
	private Date createdDate;

	/** The modified date. */
	private Date modifiedDate;
	
	/** The version. */
	private String version = "1.0.0";

	public UnsupportedUser(String partnerId, String email, String state) {
		super();
		this.partnerId = partnerId;
		this.email = email;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
