package org.runimo.runimo.runimo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;
import org.runimo.runimo.item.domain.EggType;

@Entity
@Table(name = "runimo_definition")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RunimoDefinition extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "img_url")
    private String imgUrl;


    @OneToOne
    @JoinColumn(name = "egg_type_id", nullable = false)
    private EggType type;

    @Builder
    public RunimoDefinition(String name, String code, String description, String imgUrl,
        EggType type) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.imgUrl = imgUrl;
        this.type = type;
    }
}
