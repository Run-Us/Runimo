package org.runimo.runimo.user.service.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.item.service.ItemActivityCreator;
import org.runimo.runimo.user.UserItemFixtures;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dto.command.UseItemCommand;
import org.runimo.runimo.user.service.dto.response.UseItemResponse;
import org.runimo.runimo.user.service.usecases.items.UseItemUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("test")
@SpringBootTest
class UseItemUsecaseTest {

    @MockitoBean
    private UserItemFinder userItemFinder;

    @MockitoBean
    private ItemActivityCreator itemActivityCreator;

    @Autowired
    private UseItemUsecase useItemUsecase;

    @Test
    void 아이템_사용_유즈케이스_테스트() {
        //given
        UseItemCommand command = new UseItemCommand(1L, 1L, 10L);
        when(userItemFinder.findByUserIdAndItemIdWithXLock(any(), any()))
            .thenReturn(Optional.ofNullable(UserItemFixtures.getUserItemWithQuantity(10L)));
        //when
        UseItemResponse res = useItemUsecase.useItem(command);

        //then
        verify(itemActivityCreator, times(1)).createItemActivity(any());
        assertNotNull(res);
        assertEquals(command.itemId(), res.itemId());
        assertEquals(0, res.quantity());
    }
}