package com.surroundinsurance.user.service.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.surroundinsurance.user.service.controller.dto.AuthenticationByProviderRQ;
import com.surroundinsurance.user.service.controller.dto.AuthenticationByProviderRS;
import com.surroundinsurance.user.service.controller.dto.AuthenticationRQ;
import com.surroundinsurance.user.service.controller.dto.AuthenticationRS;
import com.surroundinsurance.user.service.controller.dto.DomainToDtoTransformer;
import com.surroundinsurance.user.service.controller.dto.Provider;
import com.surroundinsurance.user.service.controller.dto.TokenVerificationRS;
import com.surroundinsurance.user.service.domain.partner.strategy.PartnerSpecificationRetrievalStrategy;
import com.surroundinsurance.user.service.domain.user.AdditionalInformation;
import com.surroundinsurance.user.service.domain.user.UserAuthenticationToken;
import com.surroundinsurance.user.service.domain.user.strategy.UserAuthenticationStrategy;
import com.surroundinsurance.user.service.infrastructure.service.ProviderAuthenticationGatewayService;
import com.surroundinsurance.user.service.platform.common.CommonConstants;
import com.surroundinsurance.user.service.platform.common.LoggingEvent;
import com.surroundinsurance.user.service.platform.common.LoggingUtil;

/**
 * The Class UserAuthenticationApplicationServiceImpl.
 * 
 * @author shanakak
 */
@Service
public class UserAuthenticationApplicationServiceImpl implements UserAuthenticationApplicationService {

    /** The logger. */
    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserAuthenticationApplicationServiceImpl.class);
    
    /** The user authentication strategy. */
	@Autowired
	private UserAuthenticationStrategy userAuthenticationStrategy;
	
	/** The user request validator. */
	@Autowired
	private UserRequestValidator userRequestValidator;
	
	/** The partner specification retrieval strategy. */
	@Autowired
    private PartnerSpecificationRetrievalStrategy partnerSpecificationRetrievalStrategy;
	
	@Autowired
    private ProviderAuthenticationGatewayService providerAuthenticationGatewayService;
	
    @Override
    public AuthenticationRS authenticate(AuthenticationRQ authenticationRQ, String partnerId) {

        userRequestValidator.validatePartnerId(partnerId);
        Assert.hasText(authenticationRQ.getUsername(), "Username is required.");
        Assert.hasText(authenticationRQ.getPassword(), "Password is required.");

        long authTokenTimeout = partnerSpecificationRetrievalStrategy.retrieveUserAuthenticationTokenTimeout(partnerId);

        UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy.authenticate(partnerId,
                authenticationRQ.getUsername(), authenticationRQ.getPassword(), authTokenTimeout);

        AuthenticationRS authenticationRS = new AuthenticationRS(userAuthenticationToken.getId(), authTokenTimeout);
        return authenticationRS;
    }

	@Override
	public TokenVerificationRS verifyToken(String token, String partnerId) {
		
		userRequestValidator.validatePartnerId(partnerId);
		
		UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy.verifyToken(partnerId, token);
		TokenVerificationRS tokenVerificationRS = DomainToDtoTransformer.transformUserToTokenVerificationRS(userAuthenticationToken);
		
		Map<String, String> logMap = new HashMap<String, String>();
        logMap.put(CommonConstants.PARTNER_ID, partnerId);
        logMap.put(CommonConstants.USER_ID, userAuthenticationToken.getUserId());
        
        LoggingUtil.logInfo(logger, "Verify token success.", logMap, LoggingEvent.VERIFY_AUTHENTICATION_TOKEN);
		
		return tokenVerificationRS;
	}

	@Override
	public AuthenticationRS generateGuestUserAuthenticationToken(String partnerId) {
		
		userRequestValidator.validatePartnerId(partnerId);
		long authTokenTimeout = partnerSpecificationRetrievalStrategy.retrieveUserAuthenticationTokenTimeout(partnerId);
		
		UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy.generateGuestUserAuthenticationToken(partnerId, authTokenTimeout);
		
		AuthenticationRS authenticationRS = new AuthenticationRS(userAuthenticationToken.getId(), authTokenTimeout);
		return authenticationRS;
	}
	
	@Override
	public void removeUserAuthenticationToken(String token, String partnerId) {
		
		userRequestValidator.validatePartnerId(partnerId);
		userAuthenticationStrategy.removeUserAuthenticationToken(partnerId, token);
		
	}

    @Override
    public AuthenticationByProviderRS authenticationByProvider(String partnerId,
            AuthenticationByProviderRQ authenticationByProviderRQ) {
        userRequestValidator.validatePartnerId(partnerId);
        Assert.notNull(authenticationByProviderRQ, "Authentication by provider is required.");
        Assert.hasText(authenticationByProviderRQ.getProvider(), "Provider is required.");
        Assert.isTrue(Provider.contains(authenticationByProviderRQ.getProvider()), "Invalid Provider specified.");
        Assert.hasText(authenticationByProviderRQ.getExternalUserId(), "External user id is required.");
        Assert.hasText(authenticationByProviderRQ.getProviderToken(), "Provider token is required.");

        Map<String, String> providerAdditionalInformation = providerAuthenticationGatewayService.authenticateByProvider(authenticationByProviderRQ);
        List<AdditionalInformation> additionalInformationList = new ArrayList<AdditionalInformation>();
        
        for(String key : providerAdditionalInformation.keySet()){
            AdditionalInformation additionalInformation = new AdditionalInformation(key, providerAdditionalInformation.get(key));
            additionalInformationList.add(additionalInformation);
        }
        
        long authTokenTimeout = partnerSpecificationRetrievalStrategy.retrieveUserAuthenticationTokenTimeout(partnerId);

        UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy.authenticationByProvider(partnerId,
                authenticationByProviderRQ.getExternalUserId(), authTokenTimeout, additionalInformationList);

        AuthenticationByProviderRS authenticationByProviderRS = new AuthenticationByProviderRS(
                userAuthenticationToken.getId(), userAuthenticationToken.getUserId(), authTokenTimeout);

        Map<String, String> logMap = new HashMap<String, String>();
        logMap.put(CommonConstants.PARTNER_ID, partnerId);
        logMap.put(CommonConstants.USER_ID, userAuthenticationToken.getUserId());
        logMap.put(CommonConstants.TIMEOUT_VALUE, String.valueOf(authTokenTimeout));
        
        LoggingUtil.logInfo(logger, "Authenticate with provider success.", logMap, LoggingEvent.AUTHENTICATE_WITH_PROVIDER);
        
        return authenticationByProviderRS;
    }

}
