package org.runimo.runimo.user.controller;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.dtos.response.MyPageViewResponse;
import org.runimo.runimo.user.service.usecases.query.MyPageQueryUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageQueryUsecase myPageQueryUsecase;

    @GetMapping
    public ResponseEntity<SuccessResponse<MyPageViewResponse>> queryMyPageView(
        @UserId Long userId) {
        MyPageViewResponse response = myPageQueryUsecase.execute(userId);
        return ResponseEntity.ok(
            SuccessResponse.of(UserHttpResponseCode.MY_PAGE_DATA_FETCHED, response));
    }

}
