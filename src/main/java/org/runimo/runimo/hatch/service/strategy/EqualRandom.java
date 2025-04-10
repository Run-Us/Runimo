package org.runimo.runimo.hatch.service.strategy;

import java.security.SecureRandom;

public class EqualRandom implements HatchRandomStrategy {

    private final SecureRandom secureRandom = new SecureRandom(); // TODO : 빈으로 관리?

    private int size;

    public EqualRandom(int size) {
        this.size = size;
    }

    @Override
    public int generateNumber() {
        return secureRandom.nextInt(size);
    }

}
