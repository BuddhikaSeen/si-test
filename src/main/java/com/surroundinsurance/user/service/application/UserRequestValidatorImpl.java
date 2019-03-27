package com.surroundinsurance.user.service.application;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.surroundinsurance.user.service.controller.dto.UpdateUserRQ;
import com.surroundinsurance.user.service.controller.dto.UserRQ;
import com.surroundinsurance.user.service.domain.user.UserType;
import com.surroundinsurance.user.service.domain.user.exception.PartnerInvalidException;
import com.surroundinsurance.user.service.domain.user.exception.PartnerNotFoundException;
import com.surroundinsurance.user.service.platform.common.BadRequestException;
import com.surroundinsurance.user.service.platform.common.DateUtil;

/**
 * The Class UserRequestValidatorImpl.
 * 
 * @author shanakak
 */
@Service
public class UserRequestValidatorImpl implements UserRequestValidator {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserRequestValidatorImpl.class);

    @Value("${user.service.partnerids}")
    private String partnerIds;

    @Value("${user.service.user.email.regexp}")
    private String emailRegularExpression;

    @Value("${user.service.user.password.length}")
    private int passwordLength;

    @Value("${user.service.user.name.regexp:^[a-zA-Z-_.'\\s]+$}")
    private String nameRegularExpression;

    @Value("${user.service.user.phonenumber.regexp:^[0-9-\\s]+$}")
    private String phoneNumberRegularExpression;

    @Value("${user.service.user.date.regexp:^[0-9]+$}")
    private String dateRegularExpression;

    @Value("${user.service.user.countrycode.regexp:^[A-Z']+$}")
    private String countryCodeRegularExpression;

    @Value("${user.service.user.title.regexp:\\b[M][r][s][.]\\B|\\b[M][r][.]\\B|\\b[M][s][.]\\B}")
    private String titleRegularExpression;

    @Value("${user.service.user.phonecountrycode.regexp:[ ]*[+]?[0-9 ]+}")
    private String phoneCountryCodeRegularExpression;
    
    @Override
    public void validatePartnerId(String partnerId) {

        if (!StringUtils.hasText(partnerId)) {
            throw new PartnerNotFoundException("Required partner-id not specified in the request.");
        }

        if (StringUtils.hasText(partnerIds)) {
            Set<String> partnerIdsSet = StringUtils.commaDelimitedListToSet(partnerIds);
            if (!partnerIdsSet.contains(partnerId)) {
                throw new PartnerInvalidException("Provided partner-id is invalid.");
            }
            
        } else {
            logger.error("Partner id's have not been configured.");
            throw new PartnerNotFoundException("Partner id's have not been configured.");
        }
    }

    @Override
    public void validateUser(String partnerId, UserRQ userRQ, UserType userType) {

        validatePartnerId(partnerId);
        validateEmail(userRQ.getEmail());
        Assert.hasText(userRQ.getState(), "State is required.");
        
        // TODO: remove all these validations if not needed
        if("MA".equals(userRQ.getState())) {
            if (StringUtils.hasText(userRQ.getFirstName())) {
            	Assert.isTrue(userRQ.getFirstName().matches(nameRegularExpression), 
            			"First name contains invalid characters.");
            }
            
            if (StringUtils.hasText(userRQ.getLastName())) {
            	Assert.isTrue(userRQ.getLastName().matches(nameRegularExpression), 
            			"Last name contains invalid characters.");
            }
            
            if (StringUtils.hasText(userRQ.getPhone())) {
                Assert.isTrue(userRQ.getPhone().matches(phoneNumberRegularExpression),
                        "Phone number has invalid characters.");
            }
            
            if (StringUtils.hasText(userRQ.getDateOfBirth())) {
                DateUtil.validateDateOfBirth(userRQ.getDateOfBirth());
            }
            
            if (StringUtils.hasText(userRQ.getCountryCode())) {
                Assert.isTrue(userRQ.getCountryCode().matches(countryCodeRegularExpression),
                        "Country Code has invalid characters, use ISO format.");
                validateCountryCode(userRQ.getCountryCode());
            }
            
            if (StringUtils.hasText(userRQ.getTitle())) {
                Assert.isTrue(userRQ.getTitle().matches(titleRegularExpression), "Invalid title format (Mr. / Mrs. / Ms.)");
            }
            
            if (StringUtils.hasText(userRQ.getPhoneCountryCode())) {
                Assert.isTrue(userRQ.getPhoneCountryCode().matches(phoneCountryCodeRegularExpression),
                        "Phone country code is invalid.");
            }

            // TODO: remove this since password not required on sign up
//            if (UserType.ENROLLED.equals(userType)) {
//                validatePassword(userRQ.getPassword());
//            }
            
            validatePhoneCountryCode(userRQ.getCountryCode(), userRQ.getPhoneCountryCode(), userRQ.getPhone());
        }
        
    }

    @Override
    public void validateUpdateUser(String partnerId, UpdateUserRQ updateUserRQ) {

        validatePartnerId(partnerId);
        validateEmailOnCreateUpdateUser(updateUserRQ.getEmail());
        
        if (StringUtils.hasText(updateUserRQ.getFirstName())) {
            Assert.isTrue(updateUserRQ.getFirstName().matches(nameRegularExpression),
                    "First name contains invalid characters.");
        }

        if (StringUtils.hasText(updateUserRQ.getLastName())) {
            Assert.isTrue(updateUserRQ.getLastName().matches(nameRegularExpression),
                    "Last name contains invalid characters.");
        }

        if (StringUtils.hasText(updateUserRQ.getPhone())) {
            Assert.isTrue(updateUserRQ.getPhone().matches(phoneNumberRegularExpression),
                    "Phone number has invalid characters.");
        }

        if (StringUtils.hasText(updateUserRQ.getDateOfBirth())) {
            DateUtil.validateDateOfBirth(updateUserRQ.getDateOfBirth());
        }

        if (StringUtils.hasText(updateUserRQ.getCountryCode())) {
            Assert.isTrue(updateUserRQ.getCountryCode().matches(countryCodeRegularExpression),
                    "Country code has invalid characters, use ISO format.");
            validateCountryCode(updateUserRQ.getCountryCode());
        }
        
        if (StringUtils.hasText(updateUserRQ.getTitle())) {
            Assert.isTrue(updateUserRQ.getTitle().matches(titleRegularExpression), "Invalid title format (Mr. / Mrs. / Ms.)");
        }
        
        if (StringUtils.hasText(updateUserRQ.getPhoneCountryCode())) {
            Assert.isTrue(updateUserRQ.getPhoneCountryCode().matches(phoneCountryCodeRegularExpression),
                    "Phone country code is invalid.");
        }

        validatePhoneCountryCode(updateUserRQ.getCountryCode(), updateUserRQ.getPhoneCountryCode(), updateUserRQ.getPhone());
    }

    @Override
    public void validateEmail(String email) {

        Assert.hasText(email, "Email is required.");
        Assert.isTrue(email.matches(emailRegularExpression), "Email is not in the correct format.");

    }

    @Override
    public void validatePassword(String password) {

        Assert.hasText(password, "Password is required.");
        Assert.isTrue(password.length() >= passwordLength,
                "Password is should be minimum of " + passwordLength + " characters.");

    }

    

    private void validateEmailOnCreateUpdateUser(String email) {
        if (StringUtils.hasText(email)) {
            Assert.isTrue(email.matches(emailRegularExpression), "Email is not in the correct format.");
        }

    }
    
    private void validatePhoneCountryCode(String countryCode, String phoneCountryCode, String phoneNumber) {

        if (StringUtils.hasText(phoneNumber)) {
            Assert.hasText(phoneCountryCode, "Phone country code is required.");
            Assert.hasText(countryCode, "Country code is required for validating phone number.");

            try {

                PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                PhoneNumber phoneNumberObj = phoneNumberUtil.parse(phoneCountryCode + phoneNumber, countryCode);

                boolean isValidForCountry = phoneNumberUtil.isValidNumberForRegion(phoneNumberObj, countryCode);

                if (!isValidForCountry) {
                    throw new BadRequestException(
                            "PHONE_NUMBER_INVALID_FOR_COUNTRY_ISO_CODE",
                            "Phone number is invalid for the given country ISO code");
                }

            } catch (NumberParseException e) {
                logger.error("Error validating phone country code.", e);
                throw new BadRequestException("BAD_REQUEST",
                        "Error validating phone country code");
            }

        }

    }
    
    private void validateCountryCode(String countryCode) {

        List<String> isoCountryCodes = Arrays.asList(java.util.Locale.getISOCountries());

        if (!isoCountryCodes.contains(countryCode)) {
            throw new BadRequestException("INVALID_ISO_COUNTRY_CODE", "Invalid ISO country code");
        }

    }
    
}
