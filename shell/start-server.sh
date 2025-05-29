#!/bin/bash

# 기본 변수 설정
APP_NAME="Runimo"
JAR_FILE="build/libs/$APP_NAME.jar"
LOG_DIR="logs"

# 로그 디렉토리 생성
mkdir -p "$LOG_DIR"

# 함수: 애플리케이션 빌드
build_app() {
    echo "애플리케이션 빌드 시작..."
    ./gradlew clean bootJar

    if [ $? -eq 0 ]; then
        echo "빌드 성공: $JAR_FILE"
    else
        echo "빌드 실패"
        exit 1
    fi
}

# 함수: 애플리케이션 실행
run_app() {
    local profile=$1

    if [ -z "$profile" ]; then
        profile="dev"
    fi

    echo "프로필 $profile 로 애플리케이션 실행..."
    nohup java -Xms512m -Xmx512m \
      -jar -Dspring.profiles.active=$profile $JAR_FILE > "$LOG_DIR/$APP_NAME-$(date +%Y%m%d).log" 2>&1 &
    echo "애플리케이션 PID: $!"
    echo "$!" > "$APP_NAME.pid"
}

# 메인 로직
case "$1" in
    build)
        build_app
        ;;
    start)
        run_app "$2"
        ;;
    build-start)
        build_app
        run_app "$2"
        ;;
    *)
        echo "사용법: $0 {build|start|build-start} [profile]"
        echo "  build: 애플리케이션 빌드"
        echo "  start [profile]: 특정 프로필로 애플리케이션 실행 (기본값: local)"
        echo "  build-start [profile]: 애플리케이션 빌드 후 실행"
        exit 1
        ;;
esac

exit 0