package com.swmarastro.mykkumi.data.repository

import android.util.Log
import com.google.gson.Gson
import com.swmarastro.mykkumi.data.datasource.UserInfoDataSource
import com.swmarastro.mykkumi.data.dto.request.UpdateUserInfoRequestDTO
import com.swmarastro.mykkumi.data.util.FormDataUtil
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.swmarastro.mykkumi.domain.entity.UserInfoVO
import com.swmarastro.mykkumi.domain.repository.UserInfoRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource,
    private val authTokenDataSource: AuthTokenDataStore,
) :UserInfoRepository {

    private var authorization = authTokenDataSource.getAccessToken()

    override suspend fun getUserInfo(): UserInfoVO {
        if(authorization == null) authorization = "" // 에러 처리 필요

        return userInfoDataSource.getUserInfo(authorization!!).toEntity()
    }

    override suspend fun updateUserInfo(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO {
        if(authorization == null) authorization = "" // 에러 처리 필요

        val userInfoDTO = UpdateUserInfoRequestDTO(
            nickname = userInfo.nickname,
            profileImage = FormDataUtil.convertUriToMultipart(userInfo.profileImage), // Uri를 Multipart/form-data로 변환
            introduction = userInfo.introduction,
            categoryIds = userInfo.categoryIds
        )

        Log.d("repository", userInfoDTO.toString())

        return userInfoDataSource.updateUserInfo(
            authorization = authorization!!,
//            params = userInfoDTO
            nickname = FormDataUtil.getBody(userInfo.nickname),
            profileImage = FormDataUtil.convertUriToMultipart(userInfo.profileImage),
            introduction = FormDataUtil.getBody(userInfo.introduction),
            categoryIds = null
        ).toEntity()
    }

}