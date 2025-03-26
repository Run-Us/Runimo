package org.runimo.runimo.user.controller;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.dtos.ItemQueryResponse;
import org.runimo.runimo.user.service.usecases.MyItemQueryUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me/items")
@RequiredArgsConstructor
public class UserItemController {

  private final MyItemQueryUsecase myItemQueryUsecase;

  @GetMapping
  public ResponseEntity<SuccessResponse<ItemQueryResponse>> queryItems(
      @UserId Long userId
  ) {
    ItemQueryResponse response = myItemQueryUsecase.execute(userId);
    return ResponseEntity.ok(SuccessResponse.of(UserHttpResponseCode.MY_PAGE_DATA_FETCHED, response));
  }
}
