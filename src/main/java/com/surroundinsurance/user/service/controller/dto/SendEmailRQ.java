package com.surroundinsurance.user.service.controller.dto;

import java.util.Map;

import com.surroundinsurance.user.service.platform.common.PlatformEventName;

public class SendEmailRQ {

	private PlatformEventName platformEventName;
	
	private Map<String, String> eventAttributes;

	public SendEmailRQ(PlatformEventName platformEventName, Map<String, String> eventAttributes) {
		super();
		this.platformEventName = platformEventName;
		this.eventAttributes = eventAttributes;
	}

	public PlatformEventName getPlatformEventName() {
		return platformEventName;
	}

	public void setPlatformEventName(PlatformEventName platformEventName) {
		this.platformEventName = platformEventName;
	}

	public Map<String, String> getEventAttributes() {
		return eventAttributes;
	}

	public void setEventAttributes(Map<String, String> eventAttributes) {
		this.eventAttributes = eventAttributes;
	}
	
}
