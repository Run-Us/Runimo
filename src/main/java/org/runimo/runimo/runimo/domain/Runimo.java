package org.runimo.runimo.runimo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Entity
@Table(name = "runimo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Runimo extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RunimoType type;

    @Builder
    public Runimo(String name, String description, RunimoType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
