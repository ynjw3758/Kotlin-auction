package com.auction.auction.auth.controller

import com.auction.auction.auth.service.AuthServices
import com.auction.auction.auth.dto.request.LoginRequest
import com.auction.auction.auth.dto.response.LoginBody
import com.auction.auction.common.exception.CommonResponse
import org.slf4j.LoggerFactory
/*import org.springframework.validation.Errors*/
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = LoggerFactory.getLogger(AuthController::class.java)

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authServices: AuthServices) {

    @PostMapping("/login")
    fun Login(@RequestBody req: LoginRequest/*, errors: Errors*/): CommonResponse<LoginBody> {
          log.info("컨트롤러 데이터 : " +req)

        val Service =  authServices.Login(req)
        log.info("로그인 결과 :" + Service)
        val tokeninfo= LoginBody(access_token = "test" , exp = 10000000)

        return CommonResponse(message = "success" , code = "200",
            body = tokeninfo, errorCode = "null")
    }


}