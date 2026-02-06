package com.auction.auction.common.exception

import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@RestControllerAdvice
class GlobalExceptionHandler {

    // 커스텀 에외처리 전용
    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<CommonResponse<Nothing>> {
        val ec = ex.errorCode

        return ResponseEntity.status(ec.status).body(
            CommonResponse(
                message = ec.message,
                body = null,
                code = ec.status.value().toString(),
                errorCode = ec.code
            )
        )
    }

    // ✅ JSON 파싱 오류
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonParse(ex: HttpMessageNotReadableException): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity.badRequest().body(
            CommonResponse(
                message = "입력값이 올바르지 않습니다.",
                body = null,
                code = "400",
                errorCode = "E0011"
            )
        )
    }

    // ✅ @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity.badRequest().body(
            CommonResponse(
                message = "필수 입력값이 누락되었거나 형식이 올바르지 않습니다.",
                body = null,
                code = "400",
                errorCode = "E0012"
            )
        )
    }

    // ✅ 파라미터 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity.badRequest().body(
            CommonResponse(
                message = "요청 파라미터 형식이 잘못되었습니다.",
                body = null,
                code = "400",
                errorCode = "E0013"
            )
        )
    }

    // ✅ 예상치 못한 에러
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity.internalServerError().body(
            CommonResponse(
                message = "서버 내부 오류가 발생했습니다.",
                body = null,
                code = "500",
                errorCode = "E9999"
            )
        )
    }
}