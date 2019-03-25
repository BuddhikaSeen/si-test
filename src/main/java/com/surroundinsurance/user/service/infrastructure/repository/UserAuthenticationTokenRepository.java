package com.surroundinsurance.user.service.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.surroundinsurance.user.service.domain.user.UserAuthenticationToken;

/**
 * The Interface UserTokenRepository. This is a Redis repository
 */
@Repository
public interface UserAuthenticationTokenRepository extends CrudRepository<UserAuthenticationToken, String> {

	

}
