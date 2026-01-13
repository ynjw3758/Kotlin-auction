package com.auction.auction.auth.controller

import com.auction.auction.auth.service.AuthServices
import com.auction.auction.auth.dto.request.LoginRequest
import com.auction.auction.auth.dto.response.LoginResponse
import com.auction.auction.common.exception.CommonResponse
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
/*import org.springframework.validation.Errors*/
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

private val log = LoggerFactory.getLogger(AuthController::class.java)

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authServices: AuthServices, private val response: HttpServletResponse) {

    @PostMapping("/login")
    fun Login(@RequestBody req: LoginRequest/*, errors: Errors*/): CommonResponse<LoginResponse> {
          log.info("컨트롤러 데이터 : " +req)

        val Service =  authServices.Login(req)
        val Response = LoginResponse(accessToken = Service.accessToken , loginId = Service.loginId,
            exp = Service.exp)
        log.info("로그인 결과 :" + Service)
        val refreshCookie = ResponseCookie.from("refreshToken", Service.refreshToken)
            .httpOnly(true)
            .secure(true)              // HTTPS 환경에서
            .sameSite("Strict")        // 또는 Lax
            .path("/auth/refresh")     // refresh API에만 전송
            .maxAge(Duration.ofDays(14))
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())


        return CommonResponse(message = "success" , code = "200",
            body = Response, errorCode = "null")
    }


}