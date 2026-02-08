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
    REFRESH_NOT_FOUND(HttpStatus.UNAUTHORIZED, "E0102", "리프레시 토큰이 유효하지 않습니다."),

    // ===== OIDC / Keycloak =====

    /** state 값이 없거나 만료됨 (CSRF / 재시도 / 위조) */
    OIDC_STATE_INVALID(
    HttpStatus.UNAUTHORIZED,
    "E0110",
    "로그인 요청이 유효하지 않거나 만료되었습니다."
    ),

    /** state는 맞지만 내부 데이터가 깨짐 */
    OIDC_STATE_CORRUPTED(
    HttpStatus.UNAUTHORIZED,
    "E0111",
    "로그인 인증 정보가 손상되었습니다."
    ),

    /** Keycloak에 code → token 교환 실패 */
    OIDC_TOKEN_EXCHANGE_FAILED(
    HttpStatus.UNAUTHORIZED,
    "E0112",
    "인증 서버 토큰 발급에 실패했습니다."
    ),

    /** id_token nonce 불일치 (재생 공격 가능성) */
    OIDC_NONCE_MISMATCH(
    HttpStatus.UNAUTHORIZED,
    "E0113",
    "로그인 인증 검증에 실패했습니다."
    ),

    /** userinfo 조회 실패 */
    OIDC_USERINFO_FAILED(
    HttpStatus.UNAUTHORIZED,
    "E0114",
    "사용자 정보를 가져오지 못했습니다."
    );
}
