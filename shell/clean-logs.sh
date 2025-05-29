#!/bin/bash

LOG_DIR="../logs"
DAYS_TO_KEEP=14

echo "로그 정리 시작... (보존 기간: $DAYS_TO_KEEP일)"

# 1. 롤링된 로그 파일 중 보존 기간 초과한 로그 삭제
find "$LOG_DIR" -type f -name "app.*.log" -mtime +$DAYS_TO_KEEP -print -exec rm -f {} \;

# 2. 빈 로그 파일 삭제
find "$LOG_DIR" -type f -name "*.log" -size 0 -print -exec rm -f {} \;

echo "로그 정리 완료"
