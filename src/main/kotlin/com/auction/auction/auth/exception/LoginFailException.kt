package com.auction.auction.auth.exception

import com.auction.auction.common.exception.ApiException

class LoginFailException : ApiException(AuthErrorCode.INVALID_ID_OR_PASSWORD)