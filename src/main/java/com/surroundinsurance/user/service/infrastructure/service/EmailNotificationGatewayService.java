package com.surroundinsurance.user.service.infrastructure.service;

import static com.surroundinsurance.user.service.platform.common.CommonConstants.PARTNER_ID_HEADER;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.surroundinsurance.user.service.controller.dto.SendEmailRQ;
import com.surroundinsurance.user.service.platform.common.InternalErrorException;
import com.surroundinsurance.user.service.platform.common.PlatformEventName;
import com.surroundinsurance.user.service.platform.common.SurroundInsuranceExceptionCodes;

@Component
public class EmailNotificationGatewayService {
	
	private Logger logger = LoggerFactory.getLogger(EmailNotificationGatewayService.class);
		
	@Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;
	
	@Value("${user.service.sendgrid.connector.url}")
	private String sendgridConnectorUrl;
	
	@Async
	public void sendEmail(String partnerId, PlatformEventName platformEventName, Map<String, String> notificationEventParams) {
		SendEmailRQ sendEmailRQ = new SendEmailRQ(platformEventName, notificationEventParams);
		
		try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(sendgridConnectorUrl.concat("/sendemail"));

            HttpHeaders httpHeaders = constructHttpHeaders(partnerId);

            HttpEntity<SendEmailRQ> httpEntity = new HttpEntity<>(sendEmailRQ, httpHeaders);

            ResponseEntity<Object> p97StatusResponseEntity = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.POST, httpEntity, Object.class);
		} catch (HttpServerErrorException e) {
            logger.error("HttpServerErrorException occurred while connecting to the email connector", e);
            throw new InternalErrorException(SurroundInsuranceExceptionCodes.INTERNAL_SERVER_ERROR.toString(),
            		SurroundInsuranceExceptionCodes.INTERNAL_SERVER_ERROR.getDescription());

        } catch (HttpClientErrorException e) {
            logger.error("HttpClientErrorException occurred while connecting to the email connector", e);
            throw new InternalErrorException(SurroundInsuranceExceptionCodes.BAD_REQUEST.toString(),
            		SurroundInsuranceExceptionCodes.BAD_REQUEST.getDescription());

        } catch (ResourceAccessException e) {
            logger.error("ResourceAccessException occurred while connecting to the email connector", e);
            throw new InternalErrorException(SurroundInsuranceExceptionCodes.BAD_GATEWAY.toString(),
            		SurroundInsuranceExceptionCodes.BAD_GATEWAY.getDescription());

        } catch (Exception e) {
        	logger.error("Exception occurred while connecting to the email connector", e);
            throw new InternalErrorException(SurroundInsuranceExceptionCodes.INTERNAL_SERVER_ERROR.toString(),
            		SurroundInsuranceExceptionCodes.INTERNAL_SERVER_ERROR.getDescription());
		}
	}
	
	private HttpHeaders constructHttpHeaders(String partnerId) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(PARTNER_ID_HEADER, partnerId);

        return httpHeaders;
    }

}
