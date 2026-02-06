package com.auction.auction.common.exception

data class CommonResponse<T>(
    val message: String,
    var body: T?,
    var code: String,
    var errorCode: String? = null,
    )