package com.auction.auction.common.exception

open class ApiException(
    val errorCode: ErrorCode,
    val data: Map<String, Any?>? = null
) : RuntimeException(errorCode.message)