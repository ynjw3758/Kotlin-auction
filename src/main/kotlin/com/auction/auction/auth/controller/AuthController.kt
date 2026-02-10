package com.auction.auction.auth.controller

import com.auction.auction.auth.service.AuthServices
import com.auction.auction.auth.dto.request.LoginRequest
import com.auction.auction.auth.dto.response.LoginResponse
import com.auction.auction.common.exception.CommonResponse
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
/*import org.springframework.validation.Errors*/
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus

private val log = LoggerFactory.getLogger(AuthController::class.java)

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authServices: AuthServices, private val response: HttpServletResponse) {



    @GetMapping("/callback")
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String
    ): ResponseEntity<Void> {
        //authService.handleCallback(code, state)
         log.info("state :" + state)
         log.info("code:" + code)
        val response = authServices.completeOidcLogin(state, code);
        // 로그인 성공 후 프론트로 보내거나(302), 그냥 OK 내려도 됨
        return ResponseEntity.status(302)
            .header("Location", "http://localhost:3000/")
            .build()
    }

    @GetMapping("/login")
    fun normalLogin(): ResponseEntity<Void>{
        log.info("로그인 시작")
        val url = authServices.login();
        return ResponseEntity.status(HttpStatus.FOUND)  // 302
            .header(HttpHeaders.LOCATION, url)
            .build()
    }
    @GetMapping("kalogin")
    fun kalogin(): ResponseEntity<Void>{

        val url = authServices.kalogin();
        return ResponseEntity.status(HttpStatus.FOUND)  // 302
            .header(HttpHeaders.LOCATION, url)
            .build()
    }

/*
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


 */

}