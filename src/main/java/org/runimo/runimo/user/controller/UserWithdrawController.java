package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.service.WithdrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "USER-WITHDRAW", description = "회원 탈퇴 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserWithdrawController {

    private final WithdrawService withdrawService;

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
        @ApiResponse(responseCode = "404", description = "탈퇴하려는 유저 없음")
    })
    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(
        @UserId Long userId
    ) {
        withdrawService.withdraw(userId);
        return ResponseEntity.noContent().build();
    }
}
