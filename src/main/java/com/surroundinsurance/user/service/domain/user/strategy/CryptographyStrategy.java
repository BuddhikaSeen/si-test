package com.surroundinsurance.user.service.domain.user.strategy;

public interface CryptographyStrategy {

	String hash(CharSequence password);
	
	boolean verifyHash(CharSequence password, String hash);
	
}
