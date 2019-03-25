package com.surroundinsurance.user.service.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.surroundinsurance.user.service.domain.user.ArchivedPassword;

/**
 * The Interface ArchivedPasswordRepository.
 */
@Repository
public interface ArchivedPasswordRepository extends MongoRepository<ArchivedPassword, String> {
	
	List<ArchivedPassword> findByUserIdAndPartnerId(String userId, String partnerId, Pageable pageable);

}
