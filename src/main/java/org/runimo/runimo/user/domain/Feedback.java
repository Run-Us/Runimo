package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.CreateUpdateAuditEntity;

@Table(name = "user_feedback")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends CreateUpdateAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "rate", nullable = false)
    private Integer rate;
    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public Feedback(Long userId, Integer rate, String content) {
        this.userId = userId;
        this.rate = rate;
        this.content = content;
    }

}
