package com.swmarastro.mykkumi.data.repository

import android.util.Log
import com.swmarastro.mykkumi.data.datasource.AuthTokenDataSource
import com.swmarastro.mykkumi.data.datasource.KakaoLoginDataSource
import com.swmarastro.mykkumi.data.dto.request.KakaoLoginRequestDTO
import com.swmarastro.mykkumi.data.dto.response.MykkumiLoginResponseDTO
import com.swmarastro.mykkumi.domain.entity.KakaoToken
import com.swmarastro.mykkumi.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val authTokenDataSource: AuthTokenDataSource
) : KakaoLoginRepository {

    override suspend fun kakaoLogin(kakaoLoginToken: KakaoToken) {
        val kakaoLoginTokenRequest: KakaoLoginRequestDTO = KakaoLoginRequestDTO(
            refreshToken = kakaoLoginToken.refreshToken,
            accessToken = kakaoLoginToken.accessToken
        )
        val mykkumiLoginResponseDTO : MykkumiLoginResponseDTO = kakaoLoginDataSource.signInKakao(kakaoLoginTokenRequest)

        Log.d("test login", mykkumiLoginResponseDTO.refreshToken + " | " + mykkumiLoginResponseDTO.accessToken)

        authTokenDataSource.saveAccessToken(mykkumiLoginResponseDTO.accessToken)
        authTokenDataSource.saveRefreshToken(mykkumiLoginResponseDTO.refreshToken)

        Log.d("test EncryptedSharedPreferences", authTokenDataSource.getRefreshToken() + " | " + authTokenDataSource.getAccessToken())
    }
}