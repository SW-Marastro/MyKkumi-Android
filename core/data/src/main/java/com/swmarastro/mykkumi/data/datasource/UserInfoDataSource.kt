package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.request.UpdateUserInfoRequestDTO
import com.swmarastro.mykkumi.data.dto.response.UpdateUserInfoResponseDTO
import com.swmarastro.mykkumi.data.dto.response.UserInfoDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface UserInfoDataSource {
    @GET("api/v1/users/me")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String
    ) : UserInfoDTO

    @Multipart
    @PATCH("/api/v1/users")
    suspend fun updateUserInfo(
        @Header("Authorization") authorization: String,
//        @Body params: UpdateUserInfoRequestDTO,
        @Part("nickname") nickname: RequestBody?,
        @Part profileImage: MultipartBody.Part?,
        @Part("introduction") introduction: RequestBody?,
        @Part("categoryIds") categoryIds: RequestBody?
    ) : UpdateUserInfoResponseDTO
}