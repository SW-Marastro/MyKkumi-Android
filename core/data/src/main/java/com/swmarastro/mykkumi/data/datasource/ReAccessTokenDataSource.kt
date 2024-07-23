package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.request.ReAccessTokenRequestDTO
import com.swmarastro.mykkumi.data.dto.response.ReAccessTokenResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface ReAccessTokenDataSource {
    @POST("/api/v1/token")
    suspend fun getReAccessToken(
        @Body params: ReAccessTokenRequestDTO
    ) : ReAccessTokenResponseDTO
}