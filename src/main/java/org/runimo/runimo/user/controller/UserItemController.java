package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.controller.request.UseItemRequest;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.dtos.ItemQueryResponse;
import org.runimo.runimo.user.service.dtos.UseItemCommand;
import org.runimo.runimo.user.service.dtos.UseItemResponse;
import org.runimo.runimo.user.service.usecases.query.MyItemQueryUsecase;
import org.runimo.runimo.user.service.usecases.items.UseItemUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "USER ITEM", description = "사용자 아이템 관련 API")
@RestController
@RequestMapping("/api/v1/users/me/items")
@RequiredArgsConstructor
public class UserItemController {

  private final MyItemQueryUsecase myItemQueryUsecase;
  private final UseItemUsecase useItemUsecase;

  @Operation(summary = "내가 보유한 아이템 조회", description = "내 아이템을 조회합니다.")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "아이템 조회 성공"),
          @ApiResponse(responseCode = "401", description = "인증 실패"),
          @ApiResponse(responseCode = "403", description = "권한 없음"),
      }
  )
  @GetMapping
  public ResponseEntity<SuccessResponse<ItemQueryResponse>> queryItems(
      @UserId Long userId
  ) {
    ItemQueryResponse response = myItemQueryUsecase.queryMyAllItems(userId);
    return ResponseEntity.ok(SuccessResponse.of(UserHttpResponseCode.MY_PAGE_DATA_FETCHED, response));
  }

  @Operation(summary = "아이템 사용", description = "사용자가 아이템을 사용합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "아이템 사용 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "404", description = "아이템 없음")
  })
  @PostMapping("/use")
  public ResponseEntity<SuccessResponse<UseItemResponse>> useItem(
      @UserId Long userId,
      @Valid @RequestBody UseItemRequest request
  ) {
    UseItemResponse useItemResponse = useItemUsecase.useItem(
        new UseItemCommand(userId, request.itemId(), request.quantity())
    );
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.USE_ITEM_SUCCESS,
            useItemResponse
        ));
  }
}
