package com.surroundinsurance.user.service.domain.user.strategy;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.surroundinsurance.user.service.domain.user.VerificationCodeGenerationType;

/**
 * The Class VerificationCodeGenerationStrategy.
 */
@Service
public class VerificationCodeGenerationStrategyImpl implements VerificationCodeGenerationStrategy {

	@Override
	public String generateVerificationCode(VerificationCodeGenerationType verificationCodeGenerationType) {
		switch (verificationCodeGenerationType) {
		case UUID_GENERATION:
			return UUID.randomUUID().toString();
		default:
			throw new IllegalArgumentException("Invalid verificationCodeGenerationType provided.");
		}

	}

}
