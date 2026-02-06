package com.auction.auction.common.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val status: HttpStatus
    val code: String        // "LOGIN_FAILED", "VALIDATION_ERROR" ...
    val message: String
}