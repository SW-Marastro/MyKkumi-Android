package com.swmarastro.mykkumi.data.repository

import android.util.Log
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.data.datasource.KakaoLoginDataSource
import com.swmarastro.mykkumi.data.dto.request.KakaoLoginRequestDTO
import com.swmarastro.mykkumi.data.dto.response.MykkumiLoginResponseDTO
import com.swmarastro.mykkumi.domain.entity.KakaoToken
import com.swmarastro.mykkumi.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val authTokenDataSource: AuthTokenDataStore
) : KakaoLoginRepository {

    override suspend fun kakaoLogin(kakaoLoginToken: KakaoToken) : Boolean {
        val kakaoLoginTokenRequest: KakaoLoginRequestDTO = KakaoLoginRequestDTO(
            refreshToken = kakaoLoginToken.refreshToken,
            accessToken = kakaoLoginToken.accessToken
        )
        val mykkumiLoginResponse : MykkumiLoginResponseDTO = kakaoLoginDataSource.signInKakao(kakaoLoginTokenRequest)

        // 로그인 완료 후 token 값 받기 성공
        if(!mykkumiLoginResponse.accessToken.isNullOrEmpty() && !mykkumiLoginResponse.refreshToken.isNullOrEmpty()) {
            authTokenDataSource.saveAccessToken(mykkumiLoginResponse.accessToken)
            authTokenDataSource.saveRefreshToken(mykkumiLoginResponse.refreshToken)
            return true
        }
        // 실패
        else {
            return false
        }
    }
}