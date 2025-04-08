package org.runimo.runimo.runimo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;

@Entity
@Table(name = "runimo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Runimo extends BaseEntity {

    @Column(name = "total_run_count", nullable = false)
    private Long totalRunCount = 0L;
    @Column(name = "total_distance_in_meters", nullable = false)
    private Long totalDistanceInMeters = 0L;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "runimo_definition_id", nullable = false)
    private Long runimoDefinitionId;


    @Builder
    private Runimo(Long id, Long userId, Long runimoDefinitionId) {
        this.id = id;
        this.userId = userId;
        this.runimoDefinitionId = runimoDefinitionId;
    }

    public void validateOwner(Long userId) {
        if (!this.userId.equals(userId)) {
            throw RunimoException.of(RunimoHttpResponseCode.USER_DO_NOT_OWN_RUNIMO);
        }

    }
}
