package org.runimo.runimo.common.log.model;

import jakarta.servlet.http.HttpServletRequest;
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
        StringTokenizer st = new StringTokenizer(queryString, "&");
        Map<String, String> queryParams = new HashMap<>();
        while (st.hasMoreTokens()) {
            String[] keyVal = st.nextToken().split("=");
            queryParams.put(keyVal[0], keyVal[1]);
            // TODO : null 이면?? 1. query 변수 자체가 안붙어있음  2. 변수는 붙어있는 데 값이 없음
        }

        return HttpRequestLogInfo.builder()
            .requestMethod(request.getMethod())
            .uri(request.getRequestURI())
            .queryParams(queryParams)
            .build();
    }

}
