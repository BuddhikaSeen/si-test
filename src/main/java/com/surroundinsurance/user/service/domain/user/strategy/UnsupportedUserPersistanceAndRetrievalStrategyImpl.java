package com.surroundinsurance.user.service.domain.user.strategy;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surroundinsurance.user.service.domain.user.UnsupportedUser;
import com.surroundinsurance.user.service.domain.user.UnsupportedUserManagementService;

@Service
public class UnsupportedUserPersistanceAndRetrievalStrategyImpl implements UnsupportedUserPersistanceAndRetrievalStrategy {

	/** The logger. */
	private org.slf4j.Logger logger = LoggerFactory.getLogger(UnsupportedUserPersistanceAndRetrievalStrategyImpl.class);
	
	@Autowired
	private UnsupportedUserValidationStrategy unsupportedUserValidationStrategy;
	
	@Autowired
	private UnsupportedUserManagementService unsupportedUserManagementService;
	
	@Override
	public UnsupportedUser createUnsupportedUser(UnsupportedUser unsupportedUser) {
		UnsupportedUser existingUnsupportedUser = unsupportedUserValidationStrategy.validateEmail(unsupportedUser.getEmail(), 
				unsupportedUser.getState(), unsupportedUser.getPartnerId());

		if (existingUnsupportedUser == null) {
			unsupportedUser = unsupportedUserManagementService.createUnsupportedUser(unsupportedUser);
		} else {
			unsupportedUser = unsupportedUserManagementService.updateUnsupportedUser(existingUnsupportedUser);
		}

		return unsupportedUser;
	}

}
