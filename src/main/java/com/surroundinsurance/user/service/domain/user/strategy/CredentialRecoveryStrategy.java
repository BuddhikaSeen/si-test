package com.surroundinsurance.user.service.domain.user.strategy;

import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.VerificationCode;

/**
 * The Interface CredentialRecoveryStrategy.
 * 
 * @author shanakak
 */
public interface CredentialRecoveryStrategy {

	/**
	 * Creates the forgot password verification code.
	 *
	 * @param partnerId
	 *            the partner id
	 * @param user
	 *            the user
	 * @return the verification code
	 */
	public VerificationCode createForgotPasswordVerificationCode(String partnerId, User user);

	/**
	 * Validate forgot password verification code.
	 *
	 * @param partnerId the partner id
	 * @param code the code
	 * @return the verification code
	 */
	public VerificationCode validateForgotPasswordVerificationCode(String partnerId, String code);

}
