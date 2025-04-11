package org.runimo.runimo.user.controller;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.service.WithdrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserWithdrawController {

    private final WithdrawService withdrawService;

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(
        @UserId Long userId
    ) {
        withdrawService.execute(userId);
        return ResponseEntity.noContent().build();
    }
}
