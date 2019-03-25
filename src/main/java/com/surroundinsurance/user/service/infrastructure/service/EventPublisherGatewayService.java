package com.surroundinsurance.user.service.infrastructure.service;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "event.request.channel")
public interface EventPublisherGatewayService {

	public Future<?> publishEvent(Map<String, String> notificationEventParams);

}
