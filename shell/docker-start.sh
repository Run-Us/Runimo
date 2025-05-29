#!/bin/bash

# 기본 변수 설정
APP_NAME="runimo"
CONTAINER_NAME="${APP_NAME}"
DOCKER_USERNAME="ekgns33"  # Docker Hub 사용자명으로 변경 필요
IMAGE_NAME="${DOCKER_USERNAME}/${APP_NAME}"  # 소문자로 변환
TAG="latest"
PORT="8080"
HOST_PORT="8080"

# 함수: Docker 이미지 가져오기
pull_image() {
    echo "Docker 이미지 가져오기: ${IMAGE_NAME}:${TAG}"
    docker pull "${IMAGE_NAME}:${TAG}"

    if [ $? -ne 0 ]; then
        echo "이미지 가져오기 실패: ${IMAGE_NAME}:${TAG}"
        exit 1
    fi

    echo "이미지 가져오기 성공"
}

# 함수: 기존 컨테이너 중지 및 삭제
stop_and_remove_container() {
    if [ "$(docker ps -a -q -f name=${CONTAINER_NAME})" ]; then
        echo "기존 컨테이너 중지 및 삭제: ${CONTAINER_NAME}"
        docker stop ${CONTAINER_NAME} || true
        docker rm ${CONTAINER_NAME} || true
    fi
}

# 함수: Docker 컨테이너 실행
run_container() {
    local profile=$1

    if [ -z "$profile" ]; then
        profile="dev"
    fi

    echo "프로필 ${profile}로 Docker 컨테이너 실행..."

    docker run -d \
        --name ${CONTAINER_NAME} \
        -p ${HOST_PORT}:${PORT} \
        -v "$PWD/logs:/app/logs" \
        --env-file .env.dev \
        -e SPRING_PROFILES_ACTIVE=${profile} \
        -e JAVA_OPTS="-Xms512m -Xmx512m" \
        --restart unless-stopped \
        ${IMAGE_NAME}:${TAG}

    if [ $? -ne 0 ]; then
        echo "컨테이너 실행 실패"
        exit 1
    fi

    echo "컨테이너 실행 성공: ${CONTAINER_NAME}"
    echo "컨테이너 ID: $(docker ps -q -f name=${CONTAINER_NAME})"
}

# 함수: 컨테이너 로그 확인
show_logs() {
    if [ "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
        echo "컨테이너 로그 출력: ${CONTAINER_NAME}"
        docker logs -f ${CONTAINER_NAME}
    else
        echo "실행 중인 컨테이너가 없습니다: ${CONTAINER_NAME}"
    fi
}

# 메인 로직
case "$1" in
    pull)
        pull_image
        ;;
    start)
        stop_and_remove_container
        pull_image
        run_container "$2"
        ;;
    restart)
        stop_and_remove_container
        run_container "$2"
        ;;
    stop)
        stop_and_remove_container
        ;;
    logs)
        show_logs
        ;;
    *)
        echo "사용법: $0 {pull|start|restart|stop|logs} [profile]"
        echo "  pull: Docker 이미지 가져오기"
        echo "  start [profile]: 컨테이너 시작 (기본값: dev)"
        echo "  restart [profile]: 컨테이너 재시작"
        echo "  stop: 컨테이너 중지 및 삭제"
        echo "  logs: 컨테이너 로그 출력"
        exit 1
        ;;
esac

exit 0