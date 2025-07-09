package org.runimo.runimo.user.enums;

import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum UserHttpResponseCode implements CustomResponseCode {
    MY_PAGE_DATA_FETCHED(HttpStatus.OK, "마이페이지 데이터 조회 성공", "마이페이지 데이터 조회 성공"),
    MAIN_PAGE_DATA_FETCHED(HttpStatus.OK, "메인페이지 데이터 조회 성공", "메인페이지 데이터 조회 성공"),
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공", "회원가입 성공"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공", "로그인 성공"),
    REFRESH_SUCCESS(HttpStatus.OK, "토큰 재발급 성공", "토큰 재발급 성공"),

    USE_ITEM_SUCCESS(HttpStatus.OK, "아이템 사용 성공", "아이템 사용 성공"),
    REGISTER_EGG_SUCCESS(HttpStatus.CREATED, "부화기 등록 성공", "부화기 등록 성공"),
    USE_LOVE_POINT_SUCCESS(HttpStatus.OK, "애정 사용 성공", "애정 사용 성공"),
    MY_INCUBATING_EGG_FETCHED(HttpStatus.OK, "부화기중인 알 조회 성공", "부화중인 알 조회 성공"),
    NOTIFICATION_ALLOW_UPDATED(HttpStatus.OK, "알림 허용 업데이트 성공", "알림 허용 업데이트 성공"),
    NOTIFICATION_ALLOW_FETCHED(HttpStatus.OK, "알림 허용 조회 성공", "알림 허용 조회 성공"),
    FEEDBACK_CREATED(HttpStatus.CREATED, "피드백 생성 성공", "피드백 생성 성공"),

    LOGIN_FAIL_NOT_SIGN_IN(HttpStatus.NOT_FOUND
        , "로그인 실패 - 회원가입하지 않은 사용자", "로그인 실패 - 회원가입하지 않은 사용자"),
    LOGIN_FAIL_INVALID(HttpStatus.UNAUTHORIZED, "인증 실패", "JWT Decode실패"),
    SIGNIN_FAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "로그인 실패 - 이미 존재하는 사용자", "로그인 실패 - 이미 존재하는 사용자"),
    JWT_TOKEN_BROKEN(HttpStatus.BAD_REQUEST, "JWT 토큰이 손상되었습니다", "JWT 토큰이 손상되었습니다"),
    TOKEN_REFRESH_FAIL(HttpStatus.FORBIDDEN, "토큰 재발급 실패", "Refresh 토큰이 유효하지 않습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "인증 실패", "JWT 토큰 인증 실패"),
    REFRESH_EXPIRED(HttpStatus.FORBIDDEN, "리프레시 토큰 만료", "리프레시 토큰 만료"),
    TOKEN_DELETE_REFRESH_FAIL(HttpStatus.FORBIDDEN, "토큰 삭제 실패",
        "사용자가 유효하지 않습니다. Refresh 토큰 삭제에 실패했습니다"),
    LOG_OUT_SUCCESS(HttpStatus.OK, "로그아웃 성공", "로그아웃 성공"),
    ALREADY_LOG_OUT_SUCCESS(HttpStatus.OK, "로그아웃 성공 (이미 로그아웃된 사용자)", "로그아웃 성공 (이미 로그아웃된 사용자)"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없음", "사용자를 찾을 수 없음"),
    DEVICE_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "디바이스 토큰이 없음", "디바이스 토큰이 없음");

    private final HttpStatus code;
    private final String clientMessage;
    private final String logMessage;


    UserHttpResponseCode(HttpStatus code, String clientMessage, String logMessage) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getClientMessage() {
        return this.clientMessage;
    }

    @Override
    public String getLogMessage() {
        return this.logMessage;
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return this.code;
    }
}

