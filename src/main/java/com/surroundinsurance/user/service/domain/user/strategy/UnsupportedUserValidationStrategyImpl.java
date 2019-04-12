package com.surroundinsurance.user.service.domain.user.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surroundinsurance.user.service.domain.user.UnsupportedUser;
import com.surroundinsurance.user.service.domain.user.UnsupportedUserManagementService;

@Service
public class UnsupportedUserValidationStrategyImpl implements UnsupportedUserValidationStrategy {

	@Autowired
	private UnsupportedUserManagementService unsupportedUserManagementService;

	@Override
	public UnsupportedUser validateEmail(String email, String state, String partnerId) {

		UnsupportedUser existingUnsupportedUser = unsupportedUserManagementService.retrieveUserByEmailAndState(email, state, partnerId);
		
		return existingUnsupportedUser;

	}

}
