package com.auction.auction.auth.service

import com.auction.auction.auth.exception.AuthErrorCode
import com.auction.auction.auth.jwt.JwtProvider
import com.auction.auction.auth.oidc.KeycloakAuthUrlBuilder
import com.auction.auction.auth.oidc.KeycloakIdTokenVerifier
import com.auction.auction.auth.oidc.KeycloakOidcClient
import com.auction.auction.auth.oidc.OidcStatePayload
import com.auction.auction.common.exception.ApiException
import com.auction.auction.user.repo.UserInfoRepository
import com.auction.auction.user.service.UserCommandService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.util.Base64


private val log = LoggerFactory.getLogger(AuthServices::class.java)

@Service
class AuthServices(private final val userRepository : UserInfoRepository,
                   private final val passEncoder : PasswordEncoder,
                   private final val JwtProvider : JwtProvider,
                   private final val RedisService: RedisServices,
                   private final val OidcStateService:OidcStateService,
                   private final val KeycloakAuthUrlBuilder : KeycloakAuthUrlBuilder,
                   private final val KeycloakOidcClient : KeycloakOidcClient,
                   private final val KeycloakIdTokenVerifier: KeycloakIdTokenVerifier,
                   private final val UserCommandService : UserCommandService
                   ) {

    fun completeOidcLogin(state: String , code: String) {

        log.info("callback 로그인 처리")
        val statValid = RedisService.validateOidcState(state)
        log.info("stat 값 검증 :" + statValid)
        RedisService.consumeOidcState(state)
        // ✅ 3) code -> token 교환 (RestClient로)
        val token = KeycloakOidcClient.exchangeCodeForToken(code , statValid.codeVerifier)
        // ✅ 2) nonce 검증 (id_token 필요)
        val idToken = token.idToken ?: throw ApiException(AuthErrorCode.OIDC_NONCE_MISMATCH)
        //3) nonce 검증
        KeycloakIdTokenVerifier.verifyNonce(idToken , statValid.nonce)
        // ✅ 4) (선택) userinfo 호출
        val userInfo = KeycloakOidcClient.fetchUserInfo(token.accessToken)
        log.info("userinfo: {}", userInfo)
        // 5) (선택) userinfo 데이터 db의 저장
        UserCommandService.upsertFromKeycloak(userInfo);

    }

    fun login():String{

        //Todo
        //stat, nonce 랜덤값 생성
        //생성된 데이터 redis의 저장
        //keyloak으로 요청하기
        val stat = OidcStateService.generateState()
        val nonce = OidcStateService.generateNonce()
        log.info(stat.toString())
        log.info(nonce)
        OidcStateService.store(
            stat,
            OidcStatePayload(
                nonce = nonce,
                returnUrl = null,        // 필요하면 넣기
                codeVerifier = null      // PKCE 쓰면 넣기
            )
        )
        val keyloak_response = KeycloakAuthUrlBuilder.buildNormal(state = stat ,nonce = nonce )
        return keyloak_response

    }

/*
    fun Login(req: LoginRequest): LoginBody {
        log.info("넘어오는 데이터 :" + req.toString())
        val userSearch = userRepository.findByLoginId(req.id)//!!의미:null일 경우 NPE 동작
            ?: throw LoginFailException()//Elvis 연산자(?:)
        log.info("조회 데이터 :" + userSearch.loginId)

        //require(condition){에러 매시지} => condition == false일 경우 400응답 변환 후 message = 에러 메시지
        //require은 null체크용이 아니라 조건 검증용
        //Elvis는 null 처리 전용 아래 처럼 boolean 검즏에는 사용 불가
        //하지만 실무에서는 가독성 및 단순한 로직을 원하다고 들어서 if문을 사용한다고 한다
        /*
        require(
            passEncoder.matches(req.password,
                                        userSearch.passwordHash)
        ) { "아이디 또는 비밀번호가 올바르지 않습니다" }
        */
        if (!passEncoder.matches(req.password,
                userSearch.passwordHash)) {
            throw LoginFailException()
        }
        val AccessToken = JwtProvider.CreateAcessToken(userSearch.connectId , userSearch.loginId)
        log.info("엑세스 토큰 :" + AccessToken)
        val RefreshToken = JwtProvider.CreateRefreshToken(userSearch.connectId)
        log.info("리프래쉬 토큰 :" + RefreshToken)
        RedisService.SaveRefreshToken(userSearch.connectId ,RefreshToken)


       return LoginBody(accessToken = AccessToken.first,
           refreshToken = RefreshToken,
           loginId = userSearch.loginId,
           exp=AccessToken.second)
    }

 */
}