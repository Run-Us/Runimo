package org.runimo.runimo.user.service.usecases;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.runimo.runimo.user.service.dtos.GainItemCommand;
import org.runimo.runimo.user.service.dtos.GainItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class GainItemUsecaseTest {

  @Autowired
  UserItemRepository userItemRepository;
  @Autowired
  private GainItemUsecase gainItemUsecase;

  @Test
  void 아이템_획득_유즈케이스_동시성_테스트() throws InterruptedException {
    userItemRepository.saveAndFlush(new UserItem(1L, 1L, 0L));
    GainItemCommand command = new GainItemCommand(1L, 1L, 10L);
    int threadCount = 10;
    CountDownLatch latch = new CountDownLatch(threadCount);
    Runnable task = () -> {
      try {
        GainItemResponse response = gainItemUsecase.gainItem(command);
        assertNotNull(response);
      } finally {
        latch.countDown();
      }
    };
    for (int i = 0; i < threadCount; i++) {
      new Thread(task).start();
    }

    latch.await();

    UserItem ui = userItemRepository.findByUserIdAndItemId(1L, 1L).get();
    assertEquals(100L, ui.getQuantity());
  }
}
