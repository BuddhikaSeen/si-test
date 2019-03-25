package com.surroundinsurance.user.service.domain.partner.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.surroundinsurance.user.service.domain.partner.PartnerAuthenticationTokenExpirationSpecification;
import com.surroundinsurance.user.service.domain.partner.PartnerVerificationSpecification;
import com.surroundinsurance.user.service.infrastructure.repository.PartnerAuthenticationTokenExpirationSpecificationRepository;
import com.surroundinsurance.user.service.infrastructure.repository.PartnerVerificationSpecificationRepository;

/**
 * The Class PartnerSpecificationRetrievalStrategyImpl.
 * 
 * @author shanakak
 */
@Service
public class PartnerSpecificationRetrievalStrategyImpl implements PartnerSpecificationRetrievalStrategy {

	@Autowired
	private PartnerVerificationSpecificationRepository partnerVerificationSpecificationRepository;
	
	@Autowired
    private PartnerAuthenticationTokenExpirationSpecificationRepository partnerAuthenticationTokenExpirationSpecificationRepository;
	
	@Value("${user.service.verification.check.enabled}")
	private boolean verificationCheckEnabled;
	
	@Value("${user.service.user.authentication.token.timeout}")
    private long userAuthenticationTokenTimeout;
	
	@Override
	public boolean checkVerificationEnabled(String partnerId) {
		PartnerVerificationSpecification partnerVerificationSpecification = partnerVerificationSpecificationRepository
				.findByPartnerId(partnerId);

		if (partnerVerificationSpecification != null) {
			return partnerVerificationSpecification.isVerificationEnabled();
		}

		return verificationCheckEnabled;
	}

    @Override
    public long retrieveUserAuthenticationTokenTimeout(String partnerId) {
        PartnerAuthenticationTokenExpirationSpecification partnerAuthenticationTokenExpirationSpecification = partnerAuthenticationTokenExpirationSpecificationRepository.findByPartnerId(partnerId);
        
        if (partnerAuthenticationTokenExpirationSpecification != null) {
            return partnerAuthenticationTokenExpirationSpecification.getUserAuthenticationTokenTimeout();
        }
        
        return userAuthenticationTokenTimeout;
    }

}
