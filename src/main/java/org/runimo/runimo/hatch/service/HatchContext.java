package org.runimo.runimo.hatch.service;

import java.util.List;
import org.runimo.runimo.hatch.service.strategy.HatchRandomStrategy;
import org.runimo.runimo.runimo.domain.runimo_type.RunimoType;

public class HatchContext {

    private List<RunimoType> runimoTypePool;
    private HatchRandomStrategy hatchRandomStrategy;

    public RunimoType execute() {
        int chosenIdx = hatchRandomStrategy.generateNumber();
        return runimoTypePool.get(chosenIdx);
    }

    public void setHatchContext(List<RunimoType> runimoTypePool) {
        this.runimoTypePool = runimoTypePool;
    }

    public void setHatchStrategy(HatchRandomStrategy hatchRandomStrategy) {
        this.hatchRandomStrategy = hatchRandomStrategy;
    }
}
