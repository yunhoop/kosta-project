package com.oopsw.memberservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

	RefreshTokenEntity findByJti(String jti);
}
