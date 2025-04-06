package org.runimo.runimo.hatch.service;

import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.springframework.stereotype.Component;

@Component
public class HatchClient {

    // TODO : 로직 구현
    public Runimo getRunimoFromEgg(IncubatingEgg incubatingEgg) {
        return new Runimo("토끼_dummy", "R-100", "마당에 사는 토끼 dummy", "http://dummy", EggType.MADANG);
    }
}
