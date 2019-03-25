package com.surroundinsurance.user.service.domain.user.strategy;

import com.surroundinsurance.user.service.domain.user.User;

/**
 * The Interface UserValidationStrategy.
 */
public interface UserValidationStrategy {
	
	/**
	 * Validate email.
	 *
	 * @param email the email
	 * @param partnerId the partner id
	 * @return the user
	 */
	User validateEmail(String email, String partnerId);

}
