package com.surroundinsurance.user.service.infrastructure.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.surroundinsurance.user.service.controller.dto.AuthenticationByProviderRQ;

@MessageEndpoint
public class ProviderAuthenticationServiceActivator {

    /** The logger. */
    private org.slf4j.Logger logger = LoggerFactory.getLogger(ProviderAuthenticationServiceActivator.class);

    @ServiceActivator(inputChannel = "provider.authentication.request.channel")
    public Map<String, String> authenticateByProvider(AuthenticationByProviderRQ authenticationByProviderRQ) {

        logger.info("Invoking service");
        Map<String, String> userInfoMap =  new HashMap<String, String>();
       
        return userInfoMap;
    }

    

    

}
