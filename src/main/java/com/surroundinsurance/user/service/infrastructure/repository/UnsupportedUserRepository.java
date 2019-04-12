package com.surroundinsurance.user.service.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.surroundinsurance.user.service.domain.user.UnsupportedUser;
import com.surroundinsurance.user.service.domain.user.User;

@Repository
public interface UnsupportedUserRepository extends MongoRepository<UnsupportedUser, String> {

	public UnsupportedUser findByEmailAndStateAndPartnerId(String email, String state, String partnerId);
	
}
