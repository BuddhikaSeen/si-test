package com.surroundinsurance.user.service.domain.user.strategy;

import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.UserType;
import com.surroundinsurance.user.service.domain.user.VerificationCode;

/**
 * The Interface VerificationStrategy.
 * 
 * @author shanakak
 */
public interface VerificationStrategy {

	
	/**
	 * Creates the verification code.
	 *
	 * @param partnerId the partner id
	 * @param userType the user type
	 * @param user the user
	 * @param eventName the event name
	 * @return the verification code
	 */
	public VerificationCode createVerificationCode(String partnerId, UserType userType, User user, String eventName);
	
	/**
	 * Verify code.
	 *
	 * @param partnerId the partner id
	 * @param code the code
	 */
	public void verifyCode(String partnerId, String code);
	
	/**
	 * Expire verification code.
	 *
	 * @param verificationCode the verification code
	 */
	public void expireVerificationCode(VerificationCode verificationCode);
	
	public VerificationCode createOneTimePassword(String partnerId, UserType userType, User user, String eventName);
	
	public VerificationCode validateOneTimePasswordVerificationCode(String partnerId, String code);

	
}
