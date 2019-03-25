package com.surroundinsurance.user.service.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.surroundinsurance.user.service.domain.user.VerificationCode;
import com.surroundinsurance.user.service.domain.user.VerificationType;

/**
 * The Interface VerificationRepository.
 */
@Repository
public interface VerificationRepository extends MongoRepository<VerificationCode, String> {
		
	/**
	 * Find by code and partner id and verification type.
	 *
	 * @param code the code
	 * @param partnerId the partner id
	 * @param verificationType the verification type
	 * @return the verification code
	 */
	public VerificationCode findByCodeAndPartnerIdAndVerificationType(String code, String partnerId, VerificationType verificationType);
	
}
