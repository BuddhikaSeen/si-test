package com.surroundinsurance.user.service.domain.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.surroundinsurance.user.service.infrastructure.repository.UnsupportedUserRepository;

@Service
public class UnsupportedUserManagementServiceImpl implements UnsupportedUserManagementService {

	@Autowired
	private UnsupportedUserRepository unsupportedUserRepository;

	@Override
	public UnsupportedUser createUnsupportedUser(UnsupportedUser unsupportedUser) {

		Date createdDate = new Date();
		unsupportedUser.setCreatedDate(createdDate);
		unsupportedUser.setModifiedDate(createdDate);
		
		if (StringUtils.hasText(unsupportedUser.getEmail())) {
			unsupportedUser.setEmail(unsupportedUser.getEmail().toLowerCase());
		}
		
		return unsupportedUserRepository.save(unsupportedUser);
	}
	
	@Override
	public UnsupportedUser updateUnsupportedUser(UnsupportedUser unsupportedUser) {

		Date createdDate = new Date();
		unsupportedUser.setModifiedDate(createdDate);
		
		return unsupportedUserRepository.save(unsupportedUser);
	}
	
	@Override
	public UnsupportedUser retrieveUserByEmailAndState(String email, String state, String partnerId) {
		return unsupportedUserRepository.findByEmailAndStateAndPartnerId(email.toLowerCase(), state, partnerId);
	}

}
