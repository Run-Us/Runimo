package org.runimo.runimo.checker.controller;

import org.runimo.runimo.common.log.ServiceLog;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.exceptions.code.ExampleErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checker")
public class HealthCheckController {

    /**
     * health check api
     */
    @ServiceLog
    @GetMapping("/health-check")
    public ResponseEntity<SuccessResponse<String>> healthCheck() {
        return ResponseEntity.ok(
            SuccessResponse.of(ExampleErrorCode.SUCCESS, "Health check success !"));
    }

}
