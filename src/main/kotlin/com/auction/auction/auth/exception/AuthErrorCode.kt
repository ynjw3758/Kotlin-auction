package com.auction.auction.auth.exception

import com.auction.auction.common.exception.ErrorCode
import org.springframework.http.HttpStatus

//E0100 ~ E0199  : 인증 / 토큰 / 로그인
enum class AuthErrorCode(
     override val status: HttpStatus,
     override val code: String,        // "LOGIN_FAILED", "VALIDATION_ERROR" ...
     override val message: String) :
      ErrorCode{
    INVALID_ID_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "E0100", "아이디 또는 비밀번호가 올바르지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "E0101", "토큰이 만료되었습니다."),
    REFRESH_NOT_FOUND(HttpStatus.UNAUTHORIZED, "E0102", "리프레시 토큰이 유효하지 않습니다.");
}
