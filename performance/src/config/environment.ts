export const config = {
  baseUrl: __ENV.API_BASE_URL || 'http://localhost:8080/api/v1',
  maxUsers: parseInt(__ENV.MAX_USERS || '1'),
  testDuration: __ENV.TEST_DURATION || '2m',
  
  // Thresholds
  httpReqDurationP95: parseInt(__ENV.HTTP_REQ_DURATION_P95 || '1000'),
  httpReqFailedRate: parseFloat(__ENV.HTTP_REQ_FAILED_RATE || '0.01'),
  
  // Report settings
  enableHtmlReport: (__ENV.ENABLE_HTML_REPORT || 'true') === 'true',
};

export const thresholds = {
  http_req_failed: [`rate<${config.httpReqFailedRate}`],
  http_req_duration: [`p(95)<${config.httpReqDurationP95}`],
};
