package com.auction.auction.auth.controller

import com.auction.auction.auth.service.AuthServices
import com.auction.auction.auth.dto.request.LoginRequest
import com.auction.auction.auth.dto.response.LoginBody
import com.auction.auction.auth.dto.response.LoginResponse
import org.slf4j.LoggerFactory
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = LoggerFactory.getLogger(AuthController::class.java)

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authServices: AuthServices) {

    @PostMapping("/login")
    fun Login(@RequestBody req: LoginRequest, errors: Errors): LoginResponse<LoginBody> {
          log.info("컨트롤러 데이터 : " +req);

        val Service =  authServices.Login(req);
        val tokeninfo= LoginBody(access_token = "test" , exp = 10000000);

        return LoginResponse(message = "success" , code = "200",
            body = tokeninfo, errorCode = "null")
    }


}