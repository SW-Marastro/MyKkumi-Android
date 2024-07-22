package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.request.UpdateUserInfoRequestDTO
import com.swmarastro.mykkumi.data.dto.response.UpdateUserInfoResponseDTO
import com.swmarastro.mykkumi.data.dto.response.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserInfoDataSource {
    @GET("api/v1/users/me")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String
    ) : UserInfoDTO

    @PATCH("/api/v1/users")
    suspend fun updateUserInfo(
        @Header("Authorization") authorization: String,
        @Body params: UpdateUserInfoRequestDTO
    ) : UpdateUserInfoResponseDTO
}