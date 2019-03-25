package com.surroundinsurance.user.service.domain.user.strategy;

import com.surroundinsurance.user.service.domain.user.VerificationCodeGenerationType;

public interface VerificationCodeGenerationStrategy {

	String generateVerificationCode(VerificationCodeGenerationType verificationCodeGenerationType);
	
}
