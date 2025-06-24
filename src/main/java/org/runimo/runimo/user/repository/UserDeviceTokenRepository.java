package org.runimo.runimo.user.repository;

import org.runimo.runimo.user.domain.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {

}