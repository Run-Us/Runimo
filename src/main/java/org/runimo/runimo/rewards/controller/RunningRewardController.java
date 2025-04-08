package org.runimo.runimo.rewards.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.rewards.RewardHttpResponseCode;
import org.runimo.runimo.rewards.controller.requests.RewardClaimRequest;
import org.runimo.runimo.rewards.service.RewardService;
import org.runimo.runimo.rewards.service.dtos.RewardClaimCommand;
import org.runimo.runimo.rewards.service.dtos.RewardResponse;
import org.runimo.runimo.user.controller.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "REWARD", description = "보상 관련 API")
@RestController
@RequestMapping("/api/v1/rewards/runnings")
@RequiredArgsConstructor
public class RunningRewardController {

    private final RewardService rewardService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "404", description = "달리기 기록 존재하지 않음.")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<RewardResponse>> claimRunningReward(
        @RequestBody RewardClaimRequest request,
        @UserId Long userId
    ) {
        RewardResponse res = rewardService.claimReward(
            new RewardClaimCommand(userId, request.recordId()));
        return ResponseEntity.ok(
            SuccessResponse.of(RewardHttpResponseCode.CLAIM_REWARD_SUCESS, res));
    }
}
