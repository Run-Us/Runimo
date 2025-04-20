package org.runimo.runimo.common.log.model;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record HttpRequestLogInfo(
    String requestMethod,
    String uri,
    Map<String, String> queryParams
) {

    public static HttpRequestLogInfo of(HttpServletRequest request) {
        String queryString = request.getQueryString();

        Map<String, String> queryParams = getQueryParamMap(queryString);

        return HttpRequestLogInfo.builder()
            .requestMethod(request.getMethod())
            .uri(request.getRequestURI())
            .queryParams(queryParams)
            .build();
    }

    private static Map<String, String> getQueryParamMap(String queryString) {
        if (queryString == null || queryString.isBlank()) {
            return Collections.emptyMap();
        }

        Map<String, String> queryParams = new HashMap<>();

        String[] paramPairs = queryString.split("&");
        for (String paramPair : paramPairs) {
            String[] keyVal = paramPair.split("=", 2);

            String val = keyVal.length < 2 ? "" : keyVal[1];
            queryParams.put(keyVal[0], val);
        }

        return queryParams;
    }

}
