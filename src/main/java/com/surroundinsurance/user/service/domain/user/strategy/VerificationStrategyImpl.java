package com.surroundinsurance.user.service.domain.user.strategy;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.UserManagementService;
import com.surroundinsurance.user.service.domain.user.UserType;
import com.surroundinsurance.user.service.domain.user.VerificationChannelType;
import com.surroundinsurance.user.service.domain.user.VerificationCode;
import com.surroundinsurance.user.service.domain.user.VerificationCodeDetails;
import com.surroundinsurance.user.service.domain.user.VerificationCodeGenerationType;
import com.surroundinsurance.user.service.domain.user.VerificationService;
import com.surroundinsurance.user.service.domain.user.VerificationStatus;
import com.surroundinsurance.user.service.domain.user.VerificationType;
import com.surroundinsurance.user.service.domain.user.exception.VerificationCodeExpiredException;
import com.surroundinsurance.user.service.infrastructure.service.EmailNotificationGatewayService;
import com.surroundinsurance.user.service.infrastructure.service.EventPublisherGatewayService;
import com.surroundinsurance.user.service.platform.common.CommonConstants;

/**
 * The Class VerificationStrategyImpl.
 * 
 * @author shanakak
 */
@Service
public class VerificationStrategyImpl implements VerificationStrategy {

	/** The logger. */
	private org.slf4j.Logger logger = LoggerFactory.getLogger(VerificationStrategyImpl.class);
	
	/** The user management service. */
	@Autowired
	private UserManagementService userManagementService;
		
	/** The verification service. */
	@Autowired
	private VerificationService verificationService;
	
	@Autowired
	private VerificationCodeGenerationStrategyImpl verificationCodeGenerationStrategy;
	
	@Autowired
	private EmailNotificationGatewayService emailNotificationGatewayService;
	
	/** The event management service. */
	@Autowired
	private EventPublisherGatewayService eventPublisherGatewayService;
	
	/** The email notification enabled. */
	@Value("${user.service.user.email.notification.enabled}")
	private boolean emailNotificationEnabled;
	
	/** The email verification url. */
	@Value("${user.service.user.email.verification.url}")
	private String emailVerificationUrl;
	
	@Value("${user.service.user.email.onetime.password.url}")
	private String emailOneTimePasswordUrl;
	
	/** The verification code generation type. */
	@Value("${user.service.user.verification.code.generation.type:UUID_GENERATION}")
	private String verificationCodeGenerationType;
	
	/** The verification channel type. */
	@Value("${user.service.user.verification.channel.type:EMAIL}")
	private String verificationChannelType;
	
	@Value("${user.service.user.verification.code.expire.time}")
	private int userVerificationCodeExpireTime;
	
	@Value("${user.service.user.onetime.password.verification.code.expire.time}")
	private int oneTimePasswordVerificationCodeExpireTime;
		
	@Override
	public VerificationCodeDetails createVerificationCode(String partnerId, UserType userType, User user, String eventName) {
		// Creating verification code
		String code = verificationCodeGenerationStrategy.generateVerificationCode(VerificationCodeGenerationType.valueOf(verificationCodeGenerationType));
		VerificationCode verificationCode = new VerificationCode(user.getId(), partnerId, code,
				VerificationType.VERIFICATION, VerificationChannelType.valueOf(verificationChannelType));
		verificationCode = verificationService.createVerificationCode(verificationCode);
		
		// Creating one time password
		String password = verificationCodeGenerationStrategy.generateVerificationCode(VerificationCodeGenerationType.valueOf(verificationCodeGenerationType));
		VerificationCode oneTimePassword = new VerificationCode(user.getId(), partnerId, password,
				VerificationType.ONE_TIME_PASSWORD, VerificationChannelType.valueOf(verificationChannelType));
		oneTimePassword = verificationService.createVerificationCode(oneTimePassword);

		String verificationUrl = emailVerificationUrl + verificationCode.getCode();
		String oneTimePasswordUrl = emailOneTimePasswordUrl + oneTimePassword.getCode();
		
		if (emailNotificationEnabled) {
			Map<String, String> notificationEventParams = constructVerificationEventDetails(partnerId, user, userType,
					verificationUrl, oneTimePasswordUrl, eventName);

//			eventPublisherGatewayService.publishEvent(notificationEventParams);
			emailNotificationGatewayService.sendEmail(partnerId, notificationEventParams);
		}

		logger.debug("Email verification url : " + verificationUrl);
		logger.debug("Email one time password url : " + oneTimePasswordUrl);
		
		return new VerificationCodeDetails(verificationCode, oneTimePassword);
	}
	
	@Override
	public VerificationCode createOneTimePassword(String partnerId, UserType userType, User user, String eventName) {

		String code = verificationCodeGenerationStrategy.generateVerificationCode(VerificationCodeGenerationType.valueOf(verificationCodeGenerationType));
		
		VerificationCode verificationCode = new VerificationCode(user.getId(), partnerId, code,
				VerificationType.ONE_TIME_PASSWORD, VerificationChannelType.valueOf(verificationChannelType));
		verificationCode = verificationService.createVerificationCode(verificationCode);

//		String verificationUrl = emailVerificationUrl + verificationCode.getCode();
//		
//		if (emailNotificationEnabled) {
//			Map<String, String> notificationEventParams = constructVerificationEventDetails(partnerId, user, userType,
//					verificationUrl, eventName);
//
//			eventPublisherGatewayService.publishEvent(notificationEventParams);
//		}
//
//		logger.debug("Email verification url : " + verificationUrl);
		
		return verificationCode;
	}
	
	@Override
	public VerificationCode validateOneTimePasswordVerificationCode(String partnerId, String code) {
		VerificationCode verificationCode = verificationService.retrieveVerificationCode(partnerId, code, VerificationType.ONE_TIME_PASSWORD);
		
		Assert.notNull(verificationCode, "Invalid one time password verification code.");
		Assert.isTrue(!VerificationStatus.EXPIRED.equals(verificationCode.getVerificationStatus()), "One time password verification code is expired.");
		
		validateOneTimePasswordVerificationCodeExpiry(verificationCode);
		
		return verificationCode;
	}
		
	@Override
	public void verifyCode(String partnerId, String code) {
		
		VerificationCode verificationCode = verificationService.retrieveVerificationCode(partnerId, code, VerificationType.VERIFICATION);
		
		Assert.notNull(verificationCode, "Invalid verification code");
		Assert.isTrue(!VerificationStatus.EXPIRED.equals(verificationCode.getVerificationStatus()), "The verification code has expired");
		
		validateVerificationCodeExpiry(verificationCode);
		
		User user = userManagementService.retrieveUser(verificationCode.getPartnerId(), verificationCode.getUserId());
		
		Assert.notNull(user, "User not found");
		user.markAsVerified();
		userManagementService.updateUser(user);
		
		expireVerificationCode(verificationCode);
	}
	
	private void validateVerificationCodeExpiry(VerificationCode verificationCode) {
		Calendar expireTime = Calendar.getInstance();
		expireTime.setTime(verificationCode.getCreatedDate());
		expireTime.add(Calendar.MINUTE, userVerificationCodeExpireTime);
		Date expiryDate = expireTime.getTime();
		Date currentDate = new Date();

		if (currentDate.after(expiryDate)) {
			expireVerificationCode(verificationCode);
			throw new VerificationCodeExpiredException("User verification code is expired.");
		}

	}

	public void expireVerificationCode(VerificationCode verificationCode) {
		verificationCode.markAsExpired();
		verificationService.updateVerificationCode(verificationCode);
	}
	
	private void validateOneTimePasswordVerificationCodeExpiry(VerificationCode verificationCode) {
		Calendar expireTime = Calendar.getInstance();
		expireTime.setTime(verificationCode.getCreatedDate());
		expireTime.add(Calendar.MINUTE, oneTimePasswordVerificationCodeExpireTime);
		Date expiryDate = expireTime.getTime();
		Date currentDate = new Date();

		if (currentDate.after(expiryDate)) {
			expireVerificationCode(verificationCode);
			throw new VerificationCodeExpiredException("One time password verification code is expired.");
		}

	}
	
	/**
	 * Construct verification event details.
	 *
	 * @param partnerId the partner id
	 * @param user the user
	 * @param userType the user type
	 * @param verificationUrl the verification url
	 * @param eventName the event name
	 * @return the map
	 */
	private Map<String, String> constructVerificationEventDetails(String partnerId, User user, UserType userType,
			String verificationUrl, String oneTimePasswordUrl, String eventName) {
		Map<String, String> eventMap = new HashMap<String, String>();
//		eventMap.put(CommonConstants.PARTNER_ID, partnerId);
//		eventMap.put(CommonConstants.USER_ID, user.getId());
//		eventMap.put(CommonConstants.EMAIL, user.getEmail());
//		eventMap.put(CommonConstants.FIRST_NAME, user.getUserProfile().getFirstName());
//		eventMap.put(CommonConstants.LAST_NAME, user.getUserProfile().getLastName());
//		eventMap.put(CommonConstants.VERIFICATION_URL, verificationUrl);
//		eventMap.put(CommonConstants.PLATFORM_EVENT_NAME, eventName);
		
		eventMap.put(CommonConstants.EMAIL, user.getEmail());
		eventMap.put(CommonConstants.VERIFICATION_URL, verificationUrl);
		eventMap.put(CommonConstants.CREATE_PASSWORD_URL, oneTimePasswordUrl);
		eventMap.put(CommonConstants.IS_VERIFIED, String.valueOf(user.isVerified()));

		return eventMap;
	}
	
}
