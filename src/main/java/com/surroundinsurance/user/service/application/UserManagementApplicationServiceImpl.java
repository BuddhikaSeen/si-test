package com.surroundinsurance.user.service.application;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.surroundinsurance.user.service.controller.dto.CreatePasswordRQ;
import com.surroundinsurance.user.service.controller.dto.DomainToDtoTransformer;
import com.surroundinsurance.user.service.controller.dto.DtoToDomainTransformer;
import com.surroundinsurance.user.service.controller.dto.ForgotPasswordRQ;
import com.surroundinsurance.user.service.controller.dto.ForgotPasswordVerificationCode;
import com.surroundinsurance.user.service.controller.dto.OneTimePasswordRQ;
import com.surroundinsurance.user.service.controller.dto.ResendVerificationRQ;
import com.surroundinsurance.user.service.controller.dto.ResetPasswordRQ;
import com.surroundinsurance.user.service.controller.dto.RetrieveEmailRQ;
import com.surroundinsurance.user.service.controller.dto.UpdatePasswordRQ;
import com.surroundinsurance.user.service.controller.dto.UpdateUserRQ;
import com.surroundinsurance.user.service.controller.dto.UserProfileRS;
import com.surroundinsurance.user.service.controller.dto.UserRQ;
import com.surroundinsurance.user.service.controller.dto.UserRS;
import com.surroundinsurance.user.service.controller.dto.UserStateType;
import com.surroundinsurance.user.service.controller.dto.UserVerificationCode;
import com.surroundinsurance.user.service.domain.user.UnsupportedUser;
import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.UserAuthenticationToken;
import com.surroundinsurance.user.service.domain.user.UserDetails;
import com.surroundinsurance.user.service.domain.user.UserSecurityProfile;
import com.surroundinsurance.user.service.domain.user.UserStatus;
import com.surroundinsurance.user.service.domain.user.UserType;
import com.surroundinsurance.user.service.domain.user.VerificationCode;
import com.surroundinsurance.user.service.domain.user.VerificationCodeDetails;
import com.surroundinsurance.user.service.domain.user.strategy.CredentialRecoveryStrategy;
import com.surroundinsurance.user.service.domain.user.strategy.UnsupportedUserPersistanceAndRetrievalStrategy;
import com.surroundinsurance.user.service.domain.user.strategy.UserAuthenticationStrategy;
import com.surroundinsurance.user.service.domain.user.strategy.UserPersistanceAndRetrievalStrategy;
import com.surroundinsurance.user.service.domain.user.strategy.VerificationStrategy;
import com.surroundinsurance.user.service.platform.common.CommonConstants;
import com.surroundinsurance.user.service.platform.common.LoggingEvent;
import com.surroundinsurance.user.service.platform.common.LoggingUtil;

/**
 * The Class UserManagementApplicationServiceImpl.
 * 
 * @author shanakak
 */
@Service
public class UserManagementApplicationServiceImpl implements UserManagementApplicationService {

	/** The logger. */
	private org.slf4j.Logger logger = LoggerFactory.getLogger(UserManagementApplicationServiceImpl.class);
	
	/** The user persistance and retrieval strategy. */
	@Autowired
	private UserPersistanceAndRetrievalStrategy userPersistanceAndRetrievalStrategy;
	
	@Autowired
	private UnsupportedUserPersistanceAndRetrievalStrategy unsupportedUserPersistanceAndRetrievalStrategy;
	
	/** The verification strategy. */
	@Autowired
	private VerificationStrategy verificationStrategy;

	/** The user request validator. */
	@Autowired
	private UserRequestValidator userRequestValidator;
	
	/** The credential recovery strategy. */
	@Autowired
	private CredentialRecoveryStrategy credentialRecoveryStrategy;
	
	/** The user authentication strategy. */
	@Autowired
	private UserAuthenticationStrategy userAuthenticationStrategy;
	
	/** The user verification resent event name. */
	@Value("${user.service.user.verification.resent.event.name}")
	private String userVerificationResentEventName;

	@Override
	public UserRS createUser(String partnerId, UserRQ userRQ, UserType userType) {

		userRequestValidator.validateUser(partnerId, userRQ, userType);

		UserRS userRS = null;
		if("MA".equals(userRQ.getState())) {
			User user = DtoToDomainTransformer.transformUserDtoToDomain(partnerId, userRQ, userType);
//			UserSecurityProfile userSecurityProfile = DtoToDomainTransformer.transformUserSecurityProfileDtoToDomain(partnerId, userRQ);
			UserSecurityProfile userSecurityProfile = null;

			UserDetails userDetails = userPersistanceAndRetrievalStrategy.createUser(user, userSecurityProfile);

			Assert.notNull(user, "User creation failed.");
			
			boolean isVerified = UserStatus.PENDING_VERIFICATION.equals(userDetails.getUser().getUserStatus()) ? false : true;
			
			userRS = new UserRS(userDetails.getUser().getId(), UserStateType.IN_STATE, 
					userDetails.isNewUser(), isVerified, userDetails.getVerificationCode(), userDetails.getOneTimePassword());
		} else {
			UnsupportedUser unsupportedUser = DtoToDomainTransformer.transformUserDtoToUnsupportedUser(partnerId, userRQ);
			unsupportedUser = unsupportedUserPersistanceAndRetrievalStrategy.createUnsupportedUser(unsupportedUser);
			
			Assert.notNull(unsupportedUser, "Unsupported User creation failed.");
			userRS = new UserRS(unsupportedUser.getId(), UserStateType.OUT_OF_STATE);
		}

		return userRS;
	}

	@Override
	public void updateUser(String partnerId, UpdateUserRQ updateUserRQ, String userAuthenticationTokenValue) {

	    userRequestValidator.validatePartnerId(partnerId);
		
		UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy
				.retrieveUserAuthenticationToken(partnerId, userAuthenticationTokenValue, true);

		userRequestValidator.validateUpdateUser(partnerId, updateUserRQ);
		
		User user = userPersistanceAndRetrievalStrategy.retrieveUser(partnerId, userAuthenticationToken.getUserId(), true);
		String previousEmail = user.getEmail();
		DtoToDomainTransformer.transformUpdateUserDtoToDomain(updateUserRQ, user);

		userPersistanceAndRetrievalStrategy.updateUser(partnerId, previousEmail, user);

		userAuthenticationToken.setUsername(user.getEmail());

		if (user.getUserProfile() != null) {
			userAuthenticationToken.setFirstName(user.getUserProfile().getFirstName());
			userAuthenticationToken.setLastName(user.getUserProfile().getLastName());
		}

		userAuthenticationStrategy.updateUserAuthenticationToken(userAuthenticationToken);
		
		Map<String, String> logMap = new HashMap<String, String>();
        logMap.put(CommonConstants.PARTNER_ID, partnerId);
        logMap.put(CommonConstants.USER_ID, user.getId());
        
        LoggingUtil.logInfo(logger, "Update user success.", logMap, LoggingEvent.UPDATE_USER_PROFILE);

	}

	@Override
	public UserRS retrieveUserByEmail(String partnerId, RetrieveEmailRQ retrieveEmailRQ) {

		userRequestValidator.validatePartnerId(partnerId);
		userRequestValidator.validateEmail(retrieveEmailRQ.getEmail());

		User user = userPersistanceAndRetrievalStrategy.retrieveUserByEmail(partnerId, retrieveEmailRQ.getEmail());

		UserRS userRS = new UserRS(user.getId());
		return userRS;
	}

	@Override
	public void verifyCode(String partnerId, String code) {
		
		userRequestValidator.validatePartnerId(partnerId);
		Assert.hasText(code, "The verification code is required.");
		verificationStrategy.verifyCode(partnerId, code);

	}

	@Override
	public void updatePassword(String partnerId, UpdatePasswordRQ updatePasswordRQ, String userAuthenticationTokenValue) {
		
		userRequestValidator.validatePartnerId(partnerId);
		
		UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy
				.retrieveUserAuthenticationToken(partnerId, userAuthenticationTokenValue, true);
		
		// TODO : Refactor validations with correct name
        userRequestValidator.validatePassword(updatePasswordRQ.getPreviousPassword());
        userRequestValidator.validatePassword(updatePasswordRQ.getNewPassword());
		
		UserSecurityProfile userSecurityProfile = userPersistanceAndRetrievalStrategy.retrieveUserSecurityProfile(partnerId, userAuthenticationToken.getUserId(), true);
		String previousHashedPassword = userSecurityProfile.getPassword();
		userSecurityProfile.setPassword(updatePasswordRQ.getNewPassword());
		
		userPersistanceAndRetrievalStrategy.updateUserPassword(partnerId, userSecurityProfile, previousHashedPassword, updatePasswordRQ.getPreviousPassword());
		
	}
	
	@Override
	public void forgotPassword(String partnerId, ForgotPasswordRQ forgotPasswordRQ) {
		Assert.notNull(forgotPasswordRQ, "ForgotPasswordRQ is null.");
		userRequestValidator.validatePartnerId(partnerId);
		userRequestValidator.validateEmail(forgotPasswordRQ.getEmail());
		User user = userPersistanceAndRetrievalStrategy.retrieveUserByEmail(partnerId, forgotPasswordRQ.getEmail());
		
		credentialRecoveryStrategy.createForgotPasswordVerificationCode(user.getPartnerId(), user);
		
	}

	@Override
	public ForgotPasswordVerificationCode verifyForgotPasswordCode(String partnerId, String code) {
		userRequestValidator.validatePartnerId(partnerId);
		Assert.hasText(code, "The forgot password verification code is required.");
		VerificationCode verificationCode = credentialRecoveryStrategy.validateForgotPasswordVerificationCode(partnerId, code);
		
		userPersistanceAndRetrievalStrategy.retrieveUser(verificationCode.getPartnerId(), verificationCode.getUserId(), true);
		
		return new ForgotPasswordVerificationCode(verificationCode.getCode());
	}

	@Override
	public void resetPassword(String partnerId, ResetPasswordRQ resetPasswordRQ) {
		userRequestValidator.validatePartnerId(partnerId);
		Assert.notNull(resetPasswordRQ, "ResetPasswordRQ is null.");
		Assert.hasText(resetPasswordRQ.getCode(), "The reset password verification code is required.");
		
		userRequestValidator.validatePassword(resetPasswordRQ.getPassword());
		
		VerificationCode verificationCode = credentialRecoveryStrategy.validateForgotPasswordVerificationCode(partnerId, resetPasswordRQ.getCode());
		UserSecurityProfile userSecurityProfile = userPersistanceAndRetrievalStrategy.retrieveUserSecurityProfile(partnerId, verificationCode.getUserId(), true);
		userSecurityProfile.setPassword(resetPasswordRQ.getPassword());
				
		userPersistanceAndRetrievalStrategy.resetPassword(userSecurityProfile);
		verificationStrategy.expireVerificationCode(verificationCode);
		
	}

	@Override
	public UserVerificationCode resendVerification(String partnerId, ResendVerificationRQ resendVerificationRQ) {
		Assert.notNull(resendVerificationRQ, "ResendVerificationRQ is null.");
		userRequestValidator.validatePartnerId(partnerId);
		userRequestValidator.validateEmail(resendVerificationRQ.getEmail());
		
		User user = userPersistanceAndRetrievalStrategy.retrieveUserByEmail(partnerId, resendVerificationRQ.getEmail());
		VerificationCodeDetails verificationCodeDetails = verificationStrategy.createVerificationCode(partnerId, user.getUserType(), user, userVerificationResentEventName);
		
		return new UserVerificationCode(verificationCodeDetails.getVerificationCode().getCode(), verificationCodeDetails.getOneTimePassword().getCode());
	}

	@Override
	public UserProfileRS retrieveUserByToken(String partnerId, String userAuthenticationTokenValue) {
		userRequestValidator.validatePartnerId(partnerId);
		
		UserAuthenticationToken userAuthenticationToken = userAuthenticationStrategy
				.retrieveUserAuthenticationToken(partnerId, userAuthenticationTokenValue, true);
		User user = userPersistanceAndRetrievalStrategy.retrieveUser(partnerId, userAuthenticationToken.getUserId(), true);
		
		UserProfileRS userProfileRS = DomainToDtoTransformer.transformUserToUserProfileRS(user);
		
		Map<String, String> logMap = new HashMap<String, String>();
        logMap.put(CommonConstants.PARTNER_ID, partnerId);
        logMap.put(CommonConstants.USER_ID, user.getId());
        
        LoggingUtil.logInfo(logger, "Retrieve user profile success.", logMap, LoggingEvent.RETRIEVE_USER_PROFILE);
		
		return userProfileRS;
	}
	
	@Override
	public UserVerificationCode sendOneTimePassword(String partnerId, OneTimePasswordRQ oneTimePasswordRQ) {
		Assert.notNull(oneTimePasswordRQ, "OneTimePasswordRQ is null.");
		userRequestValidator.validatePartnerId(partnerId);
		userRequestValidator.validateEmail(oneTimePasswordRQ.getEmail());
		User user = userPersistanceAndRetrievalStrategy.retrieveUserByEmail(partnerId, oneTimePasswordRQ.getEmail());
		userPersistanceAndRetrievalStrategy.isUserSecurityProfileExists(partnerId, user.getId());
		
		VerificationCode verificationCode = verificationStrategy.createOneTimePassword(user.getPartnerId(), user.getUserType(), user, userVerificationResentEventName);
		return new UserVerificationCode(verificationCode.getCode());
	}
	
	@Override
	public UserVerificationCode verifyOneTimemPasswordCode(String partnerId, String code) {
		userRequestValidator.validatePartnerId(partnerId);
		Assert.hasText(code, "The one time password verification code is required.");
		VerificationCode verificationCode = verificationStrategy.validateOneTimePasswordVerificationCode(partnerId, code);
		
		userPersistanceAndRetrievalStrategy.retrieveUser(verificationCode.getPartnerId(), verificationCode.getUserId(), true);
		
		return new UserVerificationCode(verificationCode.getCode());
	}

	@Override
	public void createPassword(String partnerId, CreatePasswordRQ createPasswordRQ) {
		userRequestValidator.validatePartnerId(partnerId);
		Assert.notNull(createPasswordRQ, "CreatePasswordRQ is null.");
		Assert.hasText(createPasswordRQ.getCode(), "The one time password verification code is required.");
		
		userRequestValidator.validatePassword(createPasswordRQ.getPassword());
		
		VerificationCode verificationCode = verificationStrategy.validateOneTimePasswordVerificationCode(partnerId, createPasswordRQ.getCode());
		userPersistanceAndRetrievalStrategy.isUserSecurityProfileExists(verificationCode.getPartnerId(), verificationCode.getUserId());
		
		User user = userPersistanceAndRetrievalStrategy.retrieveUser(verificationCode.getPartnerId(), verificationCode.getUserId(), true);
		UserSecurityProfile userSecurityProfile = DtoToDomainTransformer.transformUserSecurityProfileDtoToDomain(partnerId, createPasswordRQ);
				
		userPersistanceAndRetrievalStrategy.createPassword(user, userSecurityProfile);
		verificationStrategy.expireVerificationCode(verificationCode);
		
	}
	
}
