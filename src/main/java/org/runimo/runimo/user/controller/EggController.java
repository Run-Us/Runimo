package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.controller.request.RegisterEggRequest;
import org.runimo.runimo.user.controller.request.UseLovePointRequest;
import org.runimo.runimo.user.service.dtos.*;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.usecases.eggs.IncubatingEggQueryUsecase;
import org.runimo.runimo.user.service.usecases.eggs.EggRegisterUsecase;
import org.runimo.runimo.user.service.usecases.eggs.GiveLovePointToEggUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "알 부화 API")
@RestController
@RequestMapping("/api/v1/users/eggs")
@RequiredArgsConstructor
public class EggController {

  private final EggRegisterUsecase eggRegisterUsecase;
  private final GiveLovePointToEggUsecase giveLovePointToEggUsecase;
  private final IncubatingEggQueryUsecase incubatingEggQueryUsecase;

  @PostMapping
  public ResponseEntity<SuccessResponse<RegisterEggResponse>> registerEgg(
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

  @PatchMapping
  public ResponseEntity<SuccessResponse<UseLovePointResponse>> useLovePoint(
      @UserId Long userId,
      @Valid @RequestBody UseLovePointRequest request
  ) {
    UseLovePointResponse useLovePointResponse = giveLovePointToEggUsecase.execute(
        new UseLovePointCommand(userId, request.itemId(), request.lovePointAmount())
    );
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.USE_LOVE_POINT_SUCCESS,
            useLovePointResponse
        ));
  }

  @GetMapping
  public ResponseEntity<SuccessResponse<QueryIncubatingEggResponse>> getEgg(
      @UserId Long userId
  ) {
    QueryIncubatingEggResponse response = incubatingEggQueryUsecase.execute(userId);
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.MY_PAGE_DATA_FETCHED,
            response
        ));
  }

}
