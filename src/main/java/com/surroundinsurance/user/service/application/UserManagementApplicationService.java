package com.surroundinsurance.user.service.application;

import com.surroundinsurance.user.service.controller.dto.AuthenticationRS;
import com.surroundinsurance.user.service.controller.dto.CreatePasswordRQ;
import com.surroundinsurance.user.service.controller.dto.ForgotPasswordRQ;
import com.surroundinsurance.user.service.controller.dto.ForgotPasswordVerificationCode;
import com.surroundinsurance.user.service.controller.dto.OneTimePasswordRQ;
import com.surroundinsurance.user.service.controller.dto.ResendVerificationRQ;
import com.surroundinsurance.user.service.controller.dto.ResetPasswordRQ;
import com.surroundinsurance.user.service.controller.dto.RetrieveEmailRQ;
import com.surroundinsurance.user.service.controller.dto.UpdatePasswordRQ;
import com.surroundinsurance.user.service.controller.dto.UpdateUserRQ;
import com.surroundinsurance.user.service.controller.dto.UserProfileRS;
import com.surroundinsurance.user.service.controller.dto.UserRQ;
import com.surroundinsurance.user.service.controller.dto.UserRS;
import com.surroundinsurance.user.service.controller.dto.UserVerificationCode;
import com.surroundinsurance.user.service.domain.user.UserType;

/**
 * The Interface UserManagementApplicationService.
 * 
 * @author shanakak
 */
public interface UserManagementApplicationService {

	/**
	 * Creates the user.
	 *
	 * @param partnerId the partner id
	 * @param userRQ the user rq
	 * @param userType the user type
	 * @return the user rs
	 */
	UserRS createUser(String partnerId, UserRQ userRQ, UserType userType);

	/**
	 * Update user.
	 *
	 * @param partnerId the partner id
	 * @param updateUserRQ the update user rq
	 * @param userAuthenticationTokenValue the user authentication token value
	 */
	void updateUser(String partnerId, UpdateUserRQ updateUserRQ, String userAuthenticationTokenValue);

	/**
	 * Retrieve user by email.
	 *
	 * @param partnerId the partner id
	 * @param retrieveEmailRQ the retrieve email rq
	 * @return the user rs
	 */
	UserRS retrieveUserByEmail(String partnerId, RetrieveEmailRQ retrieveEmailRQ);

	/**
	 * Verify code.
	 *
	 * @param code the code
	 */
	AuthenticationRS verifyCode(String partnerId, String code);

	/**
	 * Update password.
	 *
	 * @param partnerId the partner id
	 * @param updatePasswordRQ the update password rq
	 */
	void updatePassword(String partnerId, UpdatePasswordRQ updatePasswordRQ, String userAuthenticationTokenValue);

	/**
	 * Verify forgot password code.
	 *
	 * @param partnerId the partner id
	 * @param code the code
	 * @return the forgot password verification code
	 */
	ForgotPasswordVerificationCode verifyForgotPasswordCode(String partnerId, String code);

	/**
	 * Forgot password.
	 *
	 * @param partnerId the partner id
	 * @param forgotPasswordRQ the forgot password rq
	 */
	void forgotPassword(String partnerId, ForgotPasswordRQ forgotPasswordRQ);

	/**
	 * Reset password.
	 *
	 * @param partnerId the partner id
	 * @param resetPasswordRQ the reset password rq
	 */
	void resetPassword(String partnerId, ResetPasswordRQ resetPasswordRQ);

	/**
	 * Resend verification.
	 *
	 * @param partnerId the partner id
	 * @param resendVerificationRQ the resend verification rq
	 */
	UserVerificationCode resendVerification(String partnerId, ResendVerificationRQ resendVerificationRQ);

	/**
	 * Retrieve user by token.
	 *
	 * @param partnerId the partner id
	 * @param userAuthenticationTokenValue the user authentication token value
	 * @return the user profile rs
	 */
	UserProfileRS retrieveUserByToken(String partnerId, String userAuthenticationTokenValue);
	
	UserVerificationCode sendOneTimePassword(String partnerId, OneTimePasswordRQ oneTimePasswordRQ);
	
	UserVerificationCode verifyOneTimemPasswordCode(String partnerId, String code);
	
	AuthenticationRS createPassword(String partnerId, CreatePasswordRQ resetPasswordRQ);

}
