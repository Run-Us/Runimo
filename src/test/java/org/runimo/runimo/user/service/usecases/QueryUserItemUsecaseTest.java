package org.runimo.runimo.user.service.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.runimo.runimo.user.repository.MyItemRepository;
import org.runimo.runimo.user.service.dtos.InventoryItem;
import org.runimo.runimo.user.service.dtos.ItemQueryResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

/*
 * 1. 유저의 아이템을 조회한다.
 * */
class QueryUserItemUsecaseTest {

  private MyItemQueryUsecase myItemQueryUsecase;
  @Mock
  private MyItemRepository myItemRepository;

  @BeforeEach
  void setUp() {
    openMocks(this);
    myItemQueryUsecase = new MyItemQueryUsecaseImpl(myItemRepository);
  }

  @Test
  void 사용자의_보유_아이템_전체_조회() {
    // given
    Long userId = 1L;
    given(myItemRepository.findInventoryItemsByUserId(userId)).willReturn(List.of(
        new InventoryItem(1L, "마당알", 1L, "imgUrl"),
        new InventoryItem(2L, "숲알", 30L, "imgUr2l"))
    );
    // when
    ItemQueryResponse res = myItemQueryUsecase.execute(userId);
    // then
    assertNotNull(res);
    assertEquals(2, res.items().size());
  }
}
