package com.surroundinsurance.user.service.domain.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.surroundinsurance.user.service.infrastructure.repository.UserAuthenticationTokenRepository;

/**
 * The Class UserAuthenticationTokenServiceImpl.
 * 
 * @author shanakak
 */
@Service
public class UserAuthenticationTokenServiceImpl implements UserAuthenticationTokenService {
 
	/** The user authentication token repository. */
	@Autowired
	private UserAuthenticationTokenRepository userAuthenticationTokenRepository;

	@Override
	public UserAuthenticationToken saveUserAuthenticationToken(UserAuthenticationToken userAuthenticationToken) {
		Date createdDate = new Date();
		userAuthenticationToken.setCreatedDate(createdDate);
		userAuthenticationToken.setModifiedDate(createdDate);
		
		if (StringUtils.hasText(userAuthenticationToken.getUsername())) {
			userAuthenticationToken.setUsername(userAuthenticationToken.getUsername().toLowerCase());
		}
		
		UserAuthenticationToken token = userAuthenticationTokenRepository.save(userAuthenticationToken);

		return token;

	}

	@Override
	public UserAuthenticationToken retrieveUserAuthenticationToken(String partnerId, String token) {
		return userAuthenticationTokenRepository.findById(token).orElse(null);
	}

	@Override
	public void updateUserAuthenticationToken(UserAuthenticationToken userAuthenticationToken) {
		Date modifiedDate = new Date();
		userAuthenticationToken.setModifiedDate(modifiedDate);
		
		if (StringUtils.hasText(userAuthenticationToken.getUsername())) {
			userAuthenticationToken.setUsername(userAuthenticationToken.getUsername().toLowerCase());
		}
		
		userAuthenticationTokenRepository.save(userAuthenticationToken);
	}
	
	@Override
	public void removeUserAuthenticationToken(String token) {
		userAuthenticationTokenRepository.deleteById(token);
	}

}
