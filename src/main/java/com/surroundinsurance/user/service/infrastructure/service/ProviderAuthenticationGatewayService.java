package com.surroundinsurance.user.service.infrastructure.service;

import java.util.Map;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.surroundinsurance.user.service.controller.dto.AuthenticationByProviderRQ;

@MessagingGateway
public interface ProviderAuthenticationGatewayService {

    @Gateway(requestChannel = "provider.authentication.request.channel")
    public Map<String, String> authenticateByProvider(AuthenticationByProviderRQ authenticationByProviderRQ);

}
