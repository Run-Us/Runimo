package org.runimo.runimo.hatch.service;

import lombok.Getter;
import org.runimo.runimo.runimo.domain.runimo_type.ForestRunimo;
import org.runimo.runimo.runimo.domain.runimo_type.GrasslandRunimo;
import org.runimo.runimo.runimo.domain.runimo_type.MadangRunimo;
import org.runimo.runimo.runimo.domain.runimo_type.RunimoType;

@Getter
public enum EggRunimo {
    MADANG(MadangRunimo.values()),
    FOREST(ForestRunimo.values()),
    GRASSLAND(GrasslandRunimo.values()),
    ;

    private final RunimoType[] runimoTypes;

    EggRunimo(RunimoType[] runimoTypes) {
        this.runimoTypes = runimoTypes;
    }
}
