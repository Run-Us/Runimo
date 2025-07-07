package org.runimo.runimo.user.repository;

import org.runimo.runimo.user.domain.UserWithdrawReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawReasonRepository extends JpaRepository<UserWithdrawReason, Long> {

}