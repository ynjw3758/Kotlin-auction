package com.auction.auction.user.exception

import org.springframework.http.HttpStatus
import com.auction.auction.common.exception.ErrorCode

//E0200 ~ E0299  : 회원 / 계정 / 프로필
enum class UserErrorCode (
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode {

    DUPLE_ID(HttpStatus.BAD_REQUEST, "E0020", "아이디가 존재합니다."),
    DUPLE_EMAIL(HttpStatus.BAD_REQUEST, "E0022", "이메일이 존재합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E0034", "회원 정보를 찾을 수 없습니다.");
}