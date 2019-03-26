package com.surroundinsurance.user.service.domain.user.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.surroundinsurance.user.service.domain.user.UnsupportedUser;
import com.surroundinsurance.user.service.domain.user.UnsupportedUserManagementService;
import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.UserManagementService;

@Service
public class UnsupportedUserValidationStrategyImpl implements UnsupportedUserValidationStrategy {

	@Autowired
	private UnsupportedUserManagementService unsupportedUserManagementService;

	@Override
	public UnsupportedUser validateEmail(String email, String partnerId) {

		UnsupportedUser existingUnsupportedUser = unsupportedUserManagementService.retrieveUserByEmail(partnerId, email);
		Assert.isNull(existingUnsupportedUser, "Email already exist.");
		
		return existingUnsupportedUser;

	}

}
