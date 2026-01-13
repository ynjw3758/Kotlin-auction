package com.auction.auction.auth.service

import com.auction.auction.auth.dto.request.LoginRequest
import com.auction.auction.auth.dto.response.LoginResponse
import com.auction.auction.user.repo.UserInfoRepository
import io.lettuce.core.KillArgs.Builder.user
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(AuthServices::class.java)

@Service
class AuthServices(private final val userRepository : UserInfoRepository,
                   private final val passEncoder : PasswordEncoder,
                   ) {

    fun Login(req: LoginRequest): Long {
        log.info("넘어오는 데이터 :" + req.toString())
        val userSearch = userRepository.findByLoginId(req.id)//!!의미:null일 경우 NPE 동작
            ?: throw IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다");//Elvis 연산자(?:)
        log.info("조회 데이터 :" + userSearch.loginId);

        //require(condition){에러 매시지} => condition == false일 경우 400응답 변환 후 message = 에러 메시지
        //require은 null체크용이 아니라 조건 검증용
        //Elvis는 null 처리 전용 아래 처럼 boolean 검즏에는 사용 불가
        require(
            passEncoder.matches(req.password,
                                        userSearch.passwordHash)
        ) { "아이디 또는 비밀번호가 올바르지 않습니다" }
       return 100
    }
}