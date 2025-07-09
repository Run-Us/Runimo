package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.controller.request.UpdateNotificationAllowedRequst;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.dto.command.UpdateNotificationAllowedCommand;
import org.runimo.runimo.user.service.dto.response.MyPageViewResponse;
import org.runimo.runimo.user.service.dto.response.NotificationAllowedResponse;
import org.runimo.runimo.user.service.usecases.UpdateUserDetailUsecase;
import org.runimo.runimo.user.service.usecases.query.MyPageQueryUsecase;
import org.runimo.runimo.user.service.usecases.query.UserInfoQueryUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageQueryUsecase myPageQueryUsecase;
    private final UserInfoQueryUsecase userInfoQueryUsecase;
    private final UpdateUserDetailUsecase updateUserDetailUsecase;

    @GetMapping
    public ResponseEntity<SuccessResponse<MyPageViewResponse>> queryMyPageView(
        @UserId Long userId) {
        MyPageViewResponse response = myPageQueryUsecase.execute(userId);
        return ResponseEntity.ok(
            SuccessResponse.of(UserHttpResponseCode.MY_PAGE_DATA_FETCHED, response));
    }

    @Operation(summary = "회원 알림 수신 허용 여부 수정", description = "알림을 수신 여부 변경")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수신 여부 수정 성공"),
        @ApiResponse(responseCode = "404", description = "없는 사용자")
    })
    @PatchMapping("/notifications/permission")
    public ResponseEntity<SuccessResponse<Void>> updateNotificationPermission(
        @UserId Long userId,
        @RequestBody UpdateNotificationAllowedRequst request
    ) {
        updateUserDetailUsecase.updateUserNotificationAllowed(
            UpdateNotificationAllowedCommand.of(userId, request));
        return ResponseEntity.ok().body(
            SuccessResponse.messageOnly(UserHttpResponseCode.NOTIFICATION_ALLOW_UPDATED));
    }

    @Operation(summary = "회원 알림 수신 허용 여부 확인", description = "알림을 수신하는지 확인")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수신 여부 확인"),
        @ApiResponse(responseCode = "404", description = "없는 사용자 또는 토큰 미등록")
    })
    @GetMapping("/notifications/permission")
    public ResponseEntity<SuccessResponse<NotificationAllowedResponse>> queryNotificationPermission(
        @UserId Long userId
    ) {
        return ResponseEntity.ok(
            SuccessResponse.of(UserHttpResponseCode.NOTIFICATION_ALLOW_FETCHED,
                userInfoQueryUsecase.getUserNotificationAllowed(userId)));
    }

}
