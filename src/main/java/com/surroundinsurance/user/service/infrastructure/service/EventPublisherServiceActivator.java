package com.surroundinsurance.user.service.infrastructure.service;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.surroundinsurance.user.service.platform.common.CommonConstants;

@MessageEndpoint
public class EventPublisherServiceActivator {

	/** The logger. */
	private org.slf4j.Logger logger = LoggerFactory.getLogger(EventPublisherServiceActivator.class);
	
	@Value("${rabbitmq.platform.events.exchangename}")
    private String platformEventsExchangeName;
	
	@ServiceActivator(inputChannel = "event.request.channel")
	public Map<String, String> publishEvent(Map<String, String> notificationEventParams) {

		String eventName = notificationEventParams.get(CommonConstants.PLATFORM_EVENT_NAME);
		String partnerId = notificationEventParams.get(CommonConstants.PARTNER_ID);
		String userId = notificationEventParams.get(CommonConstants.USER_ID);
		String firstName = notificationEventParams.get(CommonConstants.FIRST_NAME);
		String email = notificationEventParams.get(CommonConstants.EMAIL);
		String verificationUrl = notificationEventParams.get(CommonConstants.VERIFICATION_URL);



		return notificationEventParams;
	}
}
