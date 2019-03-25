package com.surroundinsurance.user.service.domain.user.strategy;

/**
 * The Interface PasswordGenerationStrategy.
 * 
 * @author shanakak
 */
public interface PasswordGenerationStrategy {
	
	/**
	 * Generate password.
	 *
	 * @return the string
	 */
	public String generatePassword();

}
