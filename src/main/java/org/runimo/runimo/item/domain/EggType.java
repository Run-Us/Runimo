package org.runimo.runimo.item.domain;

import static org.runimo.runimo.common.GlobalConsts.EMPTYFIELD;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Table(name = "egg_type")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EggType extends BaseEntity {

    public static final EggType EMPTY = new EggType(EMPTYFIELD, EMPTYFIELD, 0L, 0);

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "required_distance_in_meters", nullable = false)
    private Long requiredDistanceInMeters;
    @Column(name = "level", nullable = false)
    private Integer level;

    @Builder
    private EggType(String name, String code, Long requiredDistanceInMeters, Integer eggLevel) {
        this.name = name;
        this.code = code;
        if (requiredDistanceInMeters < 0) {
            throw new IllegalArgumentException("알의 요구 거리(미터)는 0보다 작을 수 없습니다.");
        }
        this.requiredDistanceInMeters = requiredDistanceInMeters;
        if (eggLevel < 0) {
            throw new IllegalArgumentException("알의 레벨은 0보다 작을 수 없습니다.");
        }
        this.level = eggLevel;
    }

    public static EggType of(String name, String code, Long requiredDistanceInMeters,
        Integer eggLevel) {
        return EggType.builder()
            .name(name)
            .code(code)
            .requiredDistanceInMeters(requiredDistanceInMeters)
            .eggLevel(eggLevel)
            .build();
    }

}
