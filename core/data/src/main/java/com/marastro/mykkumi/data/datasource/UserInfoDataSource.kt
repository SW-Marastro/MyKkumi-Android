package com.marastro.mykkumi.data.datasource

import com.marastro.mykkumi.data.dto.request.UpdateUserInfoRequestDTO
import com.marastro.mykkumi.data.dto.response.UpdateUserInfoResponseDTO
import com.marastro.mykkumi.data.dto.response.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserInfoDataSource {
    @GET("api/v1/users/me")
    suspend fun getUserInfo(
    ) : UserInfoDTO

    @PATCH("/api/v1/users")
    suspend fun updateUserInfo(
        @Body params: UpdateUserInfoRequestDTO,
    ) : UpdateUserInfoResponseDTO
}