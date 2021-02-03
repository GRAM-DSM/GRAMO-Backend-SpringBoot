package com.gramo.gramo.entity.refreshtoken;

import org.springframework.data.repository.CrudRepository;

import java.sql.Ref;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
