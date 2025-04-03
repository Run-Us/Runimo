package org.runimo.runimo.runimo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;
import org.runimo.runimo.item.domain.EggType;

@Entity
@Table(name = "runimo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Runimo extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "egg_type", nullable = false)
    private EggType type;

    @Builder
    public Runimo(String name, String code, String description, EggType type) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.type = type;
    }
}
