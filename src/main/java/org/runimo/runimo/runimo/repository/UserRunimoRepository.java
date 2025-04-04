package org.runimo.runimo.runimo.repository;

import org.runimo.runimo.runimo.domain.UserRunimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRunimoRepository extends JpaRepository<UserRunimo, Long> {

}
