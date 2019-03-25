package com.surroundinsurance.user.service.domain.user.strategy;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.surroundinsurance.user.service.platform.common.CommonConstants;

/**
 * The Class AuthTokenGenerationStrategyImpl.
 * 
 */
@Service
public class AuthTokenGenerationStrategyImpl implements AuthTokenGenerationStrategy {

	@Override
	public String generateToken() {
		return UUID.randomUUID().toString().replace(CommonConstants.DASH, CommonConstants.EMPTY);
	}

}
