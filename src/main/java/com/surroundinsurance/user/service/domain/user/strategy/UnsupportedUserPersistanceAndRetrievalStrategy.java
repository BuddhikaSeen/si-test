package com.surroundinsurance.user.service.domain.user.strategy;

import com.surroundinsurance.user.service.domain.user.UnsupportedUser;

public interface UnsupportedUserPersistanceAndRetrievalStrategy {
	
	UnsupportedUser createUnsupportedUser(UnsupportedUser unsupportedUser);
	
}
