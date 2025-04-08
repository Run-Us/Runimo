package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.controller.request.RegisterEggRequest;
import org.runimo.runimo.user.controller.request.UseLovePointRequest;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.dtos.ItemQueryResponse;
import org.runimo.runimo.user.service.dtos.QueryIncubatingEggResponse;
import org.runimo.runimo.user.service.dtos.RegisterEggCommand;
import org.runimo.runimo.user.service.dtos.RegisterEggResponse;
import org.runimo.runimo.user.service.dtos.UseLovePointCommand;
import org.runimo.runimo.user.service.dtos.UseLovePointResponse;
import org.runimo.runimo.user.service.usecases.eggs.EggRegisterUsecase;
import org.runimo.runimo.user.service.usecases.eggs.GiveLovePointToEggUsecase;
import org.runimo.runimo.user.service.usecases.eggs.IncubatingEggQueryUsecase;
import org.runimo.runimo.user.service.usecases.query.MyItemQueryUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "알 부화 API")
@RestController
@RequestMapping("/api/v1/users/eggs")
@RequiredArgsConstructor
public class EggController {

    private final EggRegisterUsecase eggRegisterUsecase;
    private final GiveLovePointToEggUsecase giveLovePointToEggUsecase;
    private final IncubatingEggQueryUsecase incubatingEggQueryUsecase;
    private final MyItemQueryUsecase myItemQueryUsecase;

    @Operation(summary = "알 등록", description = "사용자가 알을 부화기에 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "알 등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<RegisterEggResponse>> registerEggToIncubator(
        @UserId Long userId,
        @Valid @RequestBody RegisterEggRequest request
    ) {
        RegisterEggResponse registerEggResponse = eggRegisterUsecase.execute(
            new RegisterEggCommand(userId, request.itemId())
        );
        return ResponseEntity.created(URI.create("/api/v1/users/eggs")).body(
            SuccessResponse.of(
                UserHttpResponseCode.REGISTER_EGG_SUCCESS,
                registerEggResponse
            ));
    }

    @Operation(summary = "내가 보유한 알 종류별 조회", description = "내 보유알들을 조회합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "아이템 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
        }
    )
    @GetMapping()
    public ResponseEntity<SuccessResponse<ItemQueryResponse>> queryMyEggs(
        @UserId Long userId
    ) {
        ItemQueryResponse response = myItemQueryUsecase.queryMyEggs(userId);
        return ResponseEntity.ok(
            SuccessResponse.of(UserHttpResponseCode.MY_PAGE_DATA_FETCHED, response));
    }

    @Operation(summary = "애정 포인트 사용", description = "사용자가 알에 애정 포인트를 사용합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "애정 포인트 사용 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PatchMapping
    public ResponseEntity<SuccessResponse<UseLovePointResponse>> useLovePointToEgg(
        @UserId Long userId,
        @Valid @RequestBody UseLovePointRequest request
    ) {
        UseLovePointResponse useLovePointResponse = giveLovePointToEggUsecase.execute(
            new UseLovePointCommand(userId, request.incubatingEggId(), request.lovePointAmount())
        );
        return ResponseEntity.ok().body(
            SuccessResponse.of(
                UserHttpResponseCode.USE_LOVE_POINT_SUCCESS,
                useLovePointResponse
            ));
    }

    @Operation(summary = "부화중인 알 조회", description = "사용자가 부화중인 알을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "부화중인 알 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/incubators")
    public ResponseEntity<SuccessResponse<QueryIncubatingEggResponse>> getIncubatingEgg(
        @UserId Long userId
    ) {
        QueryIncubatingEggResponse response = incubatingEggQueryUsecase.execute(userId);
        return ResponseEntity.ok().body(
            SuccessResponse.of(
                UserHttpResponseCode.MY_INCUBATING_EGG_FETCHED,
                response
            ));
    }

}
