package org.runimo.runimo.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GlobalConsts {

  public static final String TIME_ZONE_ID = "Asia/Seoul";
  public static final ZoneId ZONE_ID = ZoneId.of(TIME_ZONE_ID);
  public static final String DEFAULT_IMG_URL = "default_img_url";
  public static final String SESSION_ATTRIBUTE_USER = "user-info";
  public static final Set<String> WHITE_LIST_ENDPOINTS = Set.of(
      "/test/auth",
      "/auth",
      "/swagger-ui",
      "/v3/api-docs"
  );

}
