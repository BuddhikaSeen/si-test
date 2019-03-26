package com.surroundinsurance.user.service.domain.user;

public interface UnsupportedUserManagementService {

	UnsupportedUser createUnsupportedUser(UnsupportedUser unsupportedUser);
	
	UnsupportedUser retrieveUserByEmail(String partnerId, String email);

}
