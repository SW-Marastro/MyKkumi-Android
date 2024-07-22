package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.UserInfoDTO
import retrofit2.http.GET
import retrofit2.http.Header

interface UserInfoDataSource {
    @GET("api/v1/users/me")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String
    ) : UserInfoDTO
}