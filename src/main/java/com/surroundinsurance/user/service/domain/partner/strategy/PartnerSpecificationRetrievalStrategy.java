package com.surroundinsurance.user.service.domain.partner.strategy;

/**
 * The Interface PartnerSpecificationRetrievalStrategy.
 * 
 * @author shanakak
 */
public interface PartnerSpecificationRetrievalStrategy {
	
	/**
	 * Check verification enabled.
	 *
	 * @param partnerId the partner id
	 * @return true, if successful
	 */
	public boolean checkVerificationEnabled(String partnerId);
	
	/**
	 * Retrieve user authentication token timeout.
	 *
	 * @param partnerId the partner id
	 * @return the long
	 */
	public long retrieveUserAuthenticationTokenTimeout(String partnerId);

}
