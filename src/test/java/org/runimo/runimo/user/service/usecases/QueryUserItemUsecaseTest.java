package org.runimo.runimo.user.service.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.runimo.runimo.user.repository.MyItemRepository;
import org.runimo.runimo.user.service.dto.InventoryItem;
import org.runimo.runimo.user.service.dto.response.ItemQueryResponse;
import org.runimo.runimo.user.service.usecases.query.MyItemQueryUsecase;
import org.runimo.runimo.user.service.usecases.query.MyItemQueryUsecaseImpl;

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
        ItemQueryResponse res = myItemQueryUsecase.queryMyAllItems(userId);
        // then
        assertNotNull(res);
        assertEquals(2, res.items().size());
    }

    @Test
    void 사용자가_보유한_알_조회() {
        // given
        Long userId = 1L;
        given(myItemRepository.findMyEggsByUserId(userId)).willReturn(List.of(
            new InventoryItem(1L, "마당알", 1L, "imgUrl"),
            new InventoryItem(2L, "숲알", 30L, "imgUr2l"))
        );
        // when
        ItemQueryResponse res = myItemQueryUsecase.queryMyEggs(userId);
        // then
        assertNotNull(res);
        assertEquals(2, res.items().size());
    }
}
