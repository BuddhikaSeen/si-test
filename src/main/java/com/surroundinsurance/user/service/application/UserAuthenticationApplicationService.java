package com.surroundinsurance.user.service.application;

import com.surroundinsurance.user.service.controller.dto.AuthenticationByProviderRQ;
import com.surroundinsurance.user.service.controller.dto.AuthenticationByProviderRS;
import com.surroundinsurance.user.service.controller.dto.AuthenticationRQ;
import com.surroundinsurance.user.service.controller.dto.AuthenticationRS;
import com.surroundinsurance.user.service.controller.dto.TokenVerificationRS;

/**
 * The Interface UserAuthenticationApplicationService.
 * 
 * @author shanakak
 */
public interface UserAuthenticationApplicationService {

	/**
	 * Authenticate.
	 *
	 * @param request the request
	 * @param partnerId the partner id
	 * @return the authentication rs
	 */
	public AuthenticationRS authenticate(AuthenticationRQ request, String partnerId);
	
	/**
	 * Verify token.
	 *
	 * @param token the token
	 * @param partnerId the partner id
	 * @return the token verification rs
	 */
	public TokenVerificationRS verifyToken(String token, String partnerId);

	/**
	 * Generate guest user authentication token.
	 *
	 * @param partnerId the partner id
	 * @return the authentication rs
	 */
	public AuthenticationRS generateGuestUserAuthenticationToken(String partnerId);

	/**
	 * Authentication by provider.
	 *
	 * @param partnerId the partner id
	 * @param authenticationByProviderRQ the authentication by provider rq
	 * @return the authentication by provider rs
	 */
	public AuthenticationByProviderRS authenticationByProvider(String partnerId, AuthenticationByProviderRQ authenticationByProviderRQ);
	
	/**
	 * Removes the user authentication token.
	 *
	 * @param token the token
	 * @param partnerId the partner id
	 */
	void removeUserAuthenticationToken(String token, String partnerId);

}
