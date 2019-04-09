package com.surroundinsurance.user.service.infrastructure.service;

import static com.surroundinsurance.user.service.platform.common.CommonConstants.PARTNER_ID_HEADER;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.surroundinsurance.user.service.controller.dto.SendEmailRQ;
import com.surroundinsurance.user.service.platform.common.PlatformEventName;

@Component
public class EmailNotificationGatewayService {
		
	@Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;
	
	@Value("${user.service.sendgrid.connector.url}")
	private String sendgridConnectorUrl;
	
	public void sendEmail(String partnerId, Map<String, String> notificationEventParams) {
		SendEmailRQ sendEmailRQ = new SendEmailRQ(PlatformEventName.USER_CREATED, notificationEventParams);
		
		try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(sendgridConnectorUrl.concat("/sendemail"));

            HttpHeaders httpHeaders = constructHttpHeaders(partnerId);

            HttpEntity<SendEmailRQ> httpEntity = new HttpEntity<>(sendEmailRQ, httpHeaders);

            ResponseEntity<Object> p97StatusResponseEntity = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.POST, httpEntity, Object.class);
            
            System.out.println("Body:");
            System.out.println(p97StatusResponseEntity.getBody());
            System.out.println("Headers:");
            System.out.println(p97StatusResponseEntity.getHeaders());
		} catch (Exception e) {
			System.out.println("Error!");
			System.out.println(e);
		}
	}
	
	private HttpHeaders constructHttpHeaders(String partnerId) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(PARTNER_ID_HEADER, partnerId);

        return httpHeaders;
    }

}
