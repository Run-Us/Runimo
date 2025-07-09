package org.runimo.runimo.user.repository;

import org.runimo.runimo.user.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}