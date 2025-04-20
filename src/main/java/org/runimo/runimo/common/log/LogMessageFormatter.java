package org.runimo.runimo.common.log;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.runimo.runimo.common.log.model.HttpRequestLogInfo;
import org.runimo.runimo.common.log.model.MethodEndLogInfo;
import org.runimo.runimo.common.log.model.MethodStartLogInfo;
import org.springframework.stereotype.Component;

@Component
public class LogMessageFormatter { // TODO : 중복코드 리팩토링

    public String toHttpRequestLogMessage(HttpRequestLogInfo logInfo) {
        String queryParamString = convertMapToLogFormatString(logInfo.queryParams(),
            new StringBuilder()).toString();

        Map<String, String> logs = new LinkedHashMap<>();
        logs.put("method", logInfo.requestMethod());
        logs.put("uri", logInfo.uri());
        logs.put("query_params", queryParamString);
        logs.put("time", getCurrentTime());

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP_REQUEST ");

        convertMapToLogFormatString(logs, sb);

        return sb.toString();
    }


    public String toMethodStartLogMessage(MethodStartLogInfo logInfo) {
        String paramString = convertMapToLogFormatString(logInfo.params(),
            new StringBuilder()).toString();

        Map<String, String> logs = new LinkedHashMap<>();
        logs.put("name", logInfo.className() + "." + logInfo.methodName());
        logs.put("authenticated", String.valueOf(logInfo.authenticated()));
        logs.put("user_id", logInfo.userId());
        logs.put("params", paramString);
        logs.put("time", getCurrentTime());

        StringBuilder sb = new StringBuilder();
        sb.append("METHOD_CALL ");

        convertMapToLogFormatString(logs, sb);

        return sb.toString();
    }

    public String toMethodEndLogMessage(MethodEndLogInfo logInfo) {
        Map<String, String> logs = new LinkedHashMap<>();
        logs.put("name", logInfo.className() + "." + logInfo.methodName());
        logs.put("elapsed_time", logInfo.elapsedTimeMillis() + "ms");
        logs.put("return", logInfo.returnData());
        logs.put("time", getCurrentTime());

        StringBuilder sb = new StringBuilder();
        sb.append("METHOD_END ");

        convertMapToLogFormatString(logs, sb);

        return sb.toString();
    }

    private StringBuilder convertMapToLogFormatString(Map<String, String> infoMap,
        StringBuilder sb) {
        sb.append("[");

        Iterator<Entry<String, String>> it = infoMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            sb.append(convertCamelCaseToSnakeCase(entry.getKey()));
            sb.append("=");
            sb.append(entry.getValue());

            if (it.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb;
    }

    private String getCurrentTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    private String convertCamelCaseToSnakeCase(String camelCase) {
        return camelCase
            .replaceAll("([A-Z])(?=[A-Z])", "$1_")
            .replaceAll("([a-z])([A-Z])", "$1_$2")
            .toLowerCase();
    }
}
