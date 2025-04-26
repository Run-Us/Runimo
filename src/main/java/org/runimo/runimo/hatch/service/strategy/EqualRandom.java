package org.runimo.runimo.hatch.service.strategy;

import java.security.SecureRandom;

public class EqualRandom implements HatchRandomStrategy {

    private final SecureRandom secureRandom = new SecureRandom();

    private final int size;

    public EqualRandom(int size) {
        this.size = size;
    }

    @Override
    public int generateNumber() {
        return secureRandom.nextInt(size);
    }

}
