package org.runimo.runimo.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.scale.Distance;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GlobalConsts {

  public static final String TIME_ZONE_ID = "Asia/Seoul";
  public static final Set<String> WHITE_LIST_ENDPOINTS = Set.of(
      "/api/v1/test/auth",
      "/api/v1/auth",
      "/swagger-ui",
      "/v3/api-docs"
  );

  public static final String EMPTYFIELD = "EMPTY";
  public static final Distance DISTANCE_UNIT = new Distance(1000L);
}
