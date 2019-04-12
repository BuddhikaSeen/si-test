package com.surroundinsurance.user.service.domain.user.strategy;

import com.surroundinsurance.user.service.domain.user.UnsupportedUser;

public interface UnsupportedUserValidationStrategy {
	
	UnsupportedUser validateEmail(String email, String state, String partnerId);

}
