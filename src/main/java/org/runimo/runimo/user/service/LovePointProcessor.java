package org.runimo.runimo.user.service;

import org.runimo.runimo.user.domain.LovePoint;
import org.runimo.runimo.user.repository.LovePointRepository;
import org.springframework.stereotype.Component;

@Component
public class LovePointProcessor {

  private final LovePointRepository lovePointRepository;

  public LovePointProcessor(LovePointRepository lovePointRepository) {
    this.lovePointRepository = lovePointRepository;
  }

  // 유저의 러브포인트를 업데이트한다. XLOCK을 걸어서 동시성 문제를 해결한다.
  public LovePoint updateLovePoint(Long userId, Long loveAmount) {
    LovePoint lp = lovePointRepository.findByUserIdWithXLock(userId)
        .orElseThrow(IllegalStateException::new);
    lp.add(loveAmount);
    return lovePointRepository.save(lp);
  }
}
