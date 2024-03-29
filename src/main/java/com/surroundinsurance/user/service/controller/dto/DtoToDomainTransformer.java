package com.surroundinsurance.user.service.controller.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.surroundinsurance.user.service.domain.user.Address;
import com.surroundinsurance.user.service.domain.user.User;
import com.surroundinsurance.user.service.domain.user.UserProfile;
import com.surroundinsurance.user.service.domain.user.UserSecurityProfile;
import com.surroundinsurance.user.service.domain.user.UserSubsciption;
import com.surroundinsurance.user.service.domain.user.UserType;
import com.surroundinsurance.user.service.platform.common.CommonConstants;
import com.surroundinsurance.user.service.platform.common.DateUtil;

public class DtoToDomainTransformer {

	public static User transformUserDtoToDomain(String partnerId, UserRQ userRQ, UserType userType) {

		if (userRQ == null) {
			return null;
		}

		Address address = new Address(userRQ.getAddress1(), userRQ.getAddress2(), userRQ.getState(), userRQ.getCity(),
				userRQ.getCountryCode(), userRQ.getZipCode());
			
		UserProfile userProfile = new UserProfile(userRQ.getFirstName(), userRQ.getLastName(), userRQ.getPhone(),
				address, DateUtil.convertToDate(userRQ.getDateOfBirth()), userRQ.getTitle(), userRQ.getPhoneCountryCode());

		UserSubsciption userSubsciption = new UserSubsciption(userRQ.isSubscribedToNewsletter());

		List<com.surroundinsurance.user.service.domain.user.AdditionalInformation> additionalInformationList = constructAdditionalInformation(
				userRQ.getAdditionalInformation());

		User user = new User(partnerId, userRQ.getEmail(), userType, userProfile, userSubsciption,
				additionalInformationList, userRQ.getExternalUserId());

		return user;

	}

	private static List<com.surroundinsurance.user.service.domain.user.AdditionalInformation> constructAdditionalInformation(
			List<AdditionalInformation> additionalInformationDtoList) {
		List<com.surroundinsurance.user.service.domain.user.AdditionalInformation> additionalInformationList = null;
		
		if (additionalInformationDtoList != null && !additionalInformationDtoList.isEmpty()) {
			additionalInformationList = new ArrayList<com.surroundinsurance.user.service.domain.user.AdditionalInformation>();
			for (AdditionalInformation additionalInformationDto : additionalInformationDtoList) {
				com.surroundinsurance.user.service.domain.user.AdditionalInformation additionalInformation = new com.surroundinsurance.user.service.domain.user.AdditionalInformation(
						additionalInformationDto.getKey(), additionalInformationDto.getValue());
				additionalInformationList.add(additionalInformation);
			}
		}
		
		return additionalInformationList;
	}

	public static void transformUpdateUserDtoToDomain(UpdateUserRQ updateUserRQ, User user) {

		user.setEmail(updateUserRQ.getEmail());
		user.setUserType(user.getUserType());
		if (user.getUserProfile() == null) {
			Address address = new Address(updateUserRQ.getAddress1(), updateUserRQ.getAddress2(),
					updateUserRQ.getState(), updateUserRQ.getCity(), updateUserRQ.getCountryCode(),
					updateUserRQ.getZipCode());
			UserProfile userProfile = new UserProfile(updateUserRQ.getFirstName(), updateUserRQ.getLastName(),
					updateUserRQ.getPhone(), address, DateUtil.convertToDate(updateUserRQ.getDateOfBirth()), updateUserRQ.getTitle(), updateUserRQ.getPhoneCountryCode());
			user.setUserProfile(userProfile);
		} else {
			if (user.getUserProfile().getAddress() == null) {
				Address address = new Address(updateUserRQ.getAddress1(), updateUserRQ.getAddress2(),
						updateUserRQ.getState(), updateUserRQ.getCity(), updateUserRQ.getCountryCode(),
						updateUserRQ.getZipCode());
				user.getUserProfile().setAddress(address);
			} else {
				user.getUserProfile().getAddress().setAddress1(updateUserRQ.getAddress1());
				user.getUserProfile().getAddress().setAddress2(updateUserRQ.getAddress2());
				user.getUserProfile().getAddress().setCity(updateUserRQ.getCity());
				user.getUserProfile().getAddress().setState(updateUserRQ.getState());
				user.getUserProfile().getAddress().setZipCode(updateUserRQ.getZipCode());
				user.getUserProfile().getAddress().setCountryCode(updateUserRQ.getCountryCode());
			}
			user.getUserProfile().setFirstName(updateUserRQ.getFirstName());
			user.getUserProfile().setLastName(updateUserRQ.getLastName());
			user.getUserProfile().setDateOfBirth(DateUtil.convertToDate(updateUserRQ.getDateOfBirth()));
			user.getUserProfile().setPhone(updateUserRQ.getPhone());
			user.getUserProfile().setTitle(updateUserRQ.getTitle());
			user.getUserProfile().setPhoneCountryCode(updateUserRQ.getPhoneCountryCode());

		}

		if (user.getUserSubsciption() == null) {
			UserSubsciption userSubsciption = new UserSubsciption(updateUserRQ.isSubscribedToNewsletter());
			user.setUserSubsciption(userSubsciption);
		} else {
			user.getUserSubsciption().setSubscribedToNewsletter(updateUserRQ.isSubscribedToNewsletter());
		}

        if (updateUserRQ.getAdditionalInformation() != null && !updateUserRQ.getAdditionalInformation().isEmpty()) {
            List<com.surroundinsurance.user.service.domain.user.AdditionalInformation> newAdditionalInformationList = new ArrayList<com.surroundinsurance.user.service.domain.user.AdditionalInformation>();

            for (AdditionalInformation additionalInformationRQ : updateUserRQ.getAdditionalInformation()) {
                Assert.hasText(additionalInformationRQ.getKey(), "Additional Information key required when value is provided.");

            }

            user.getAdditionalInformation().addAll(newAdditionalInformationList);

        }

	}

	public static UserSecurityProfile transformUserSecurityProfileDtoToDomain(String partnerId, UserRQ userRQ) {

		UserSecurityProfile UserSecurityProfile = new UserSecurityProfile(partnerId, userRQ.getPassword());
		return UserSecurityProfile;
	}
}
