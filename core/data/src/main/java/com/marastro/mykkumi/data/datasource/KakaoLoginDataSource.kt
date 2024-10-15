package com.marastro.mykkumi.data.datasource

import com.marastro.mykkumi.data.dto.request.KakaoLoginRequestDTO
import com.marastro.mykkumi.data.dto.response.MykkumiLoginResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface KakaoLoginDataSource {
    @POST("/api/v1/signin/kakao")
    suspend fun signInKakao(
        @Body params: KakaoLoginRequestDTO
    ) : MykkumiLoginResponseDTO
}