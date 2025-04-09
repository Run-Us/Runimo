package org.runimo.runimo.user.domain;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.user.UserItemFixtures;


class UserItemTest {

    @Test
    void 아이템_사용_수량감소_테스트() {
        //given
        UserItem userItem = UserItemFixtures.getUserItemWithQuantity(10L);
        //when
        userItem.useItem(10L);
        //then
        assertEquals(0L, userItem.getQuantity());
    }

    @Test
    void 아이템_사용_보유_개수_초과사용시_에러_테스트() {
        //given
        UserItem userItem = UserItemFixtures.getUserItemWithQuantity(10L);

        //when
        assertThrows(IllegalArgumentException.class, () -> userItem.useItem(20L));
        assertEquals(10L, userItem.getQuantity());
    }

    @Test
    void 아이템_획득_수량증가_테스트() {
        //given
        UserItem userItem = UserItemFixtures.getUserItemWithQuantity(10L);
        //when
        userItem.gainItem(10L);
        //then
        assertEquals(20L, userItem.getQuantity());
    }
}

