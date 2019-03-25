package com.surroundinsurance.user.service.domain.user.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.UserManagementService;

/**
 * The Class UserValidationStrategyImpl.
 */
@Service
public class UserValidationStrategyImpl implements UserValidationStrategy {

	/** The user management service. */
	@Autowired
	private UserManagementService userManagementService;

	@Override
	public User validateEmail(String email, String partnerId) {

		User userExistingUser = userManagementService.retrieveUserByEmail(partnerId, email);
		Assert.isNull(userExistingUser, "Email already exist.");
		
		return userExistingUser;

	}

}
