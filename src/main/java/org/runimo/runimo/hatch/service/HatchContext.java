package org.runimo.runimo.hatch.service;

import java.util.List;
import org.runimo.runimo.hatch.service.strategy.HatchRandomStrategy;

public class HatchContext {

    private List<Long> runimoPool;
    private HatchRandomStrategy hatchRandomStrategy;

    public Long execute() {
        int chosenIdx = hatchRandomStrategy.generateNumber();
        return runimoPool.get(chosenIdx);
    }

    public void setHatchContext(List<Long> runimoIdPool) {
        this.runimoPool = runimoIdPool;
    }

    public void setHatchStrategy(HatchRandomStrategy hatchRandomStrategy) {
        this.hatchRandomStrategy = hatchRandomStrategy;
    }
}
