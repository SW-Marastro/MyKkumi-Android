package com.swmarastro.mykkumi.data.repository

import android.content.Context
import com.google.gson.Gson
import com.swmarastro.mykkumi.data.datasource.UserInfoDataSource
import com.swmarastro.mykkumi.domain.exception.ApiException
import com.swmarastro.mykkumi.data.util.FormDataUtil
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.domain.exception.ErrorResponse
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.swmarastro.mykkumi.domain.entity.UserInfoVO
import com.swmarastro.mykkumi.domain.repository.UserInfoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userInfoDataSource: UserInfoDataSource,
    private val authTokenDataSource: AuthTokenDataStore,
) :UserInfoRepository {

    private val INVALID_TOKEN = "INVALID_TOKEN"
    private val DUPLICATE_VALUE = "DUPLICATE_VALUE"

    private fun getAuthorization(): String {
        return authTokenDataSource.getAccessToken() ?: throw ApiException.InvalidTokenException()
    }

    //private var authorization = authTokenDataSource.getAccessToken()

    override suspend fun getUserInfo(): UserInfoVO {
        val authorization = getAuthorization()

        return try {
            userInfoDataSource.getUserInfo(authorization).toEntity()
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    override suspend fun updateUserInfo(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO {
        val authorization = getAuthorization()

//        val userInfoDTO = UpdateUserInfoRequestDTO(
//            nickname = userInfo.nickname,
//            profileImage = FormDataUtil.convertUriToMultipart(context, userInfo.profileImage), // Uri를 Multipart/form-data로 변환
//            introduction = userInfo.introduction,
//            categoryIds = userInfo.categoryIds
//        )
//
//        Log.d("repository", userInfoDTO.toString())

//        val imageMultipart = FormDataUtil.convertUriToMultipart(context, userInfo.profileImage)
//        Log.d("imager multipart", imageMultipart.toString())

        return try {
            userInfoDataSource.updateUserInfo(
                authorization = authorization,
//            params = userInfoDTO
                nickname = FormDataUtil.getBody(userInfo.nickname),
                profileImage = FormDataUtil.convertUriToMultipart(context, userInfo.profileImage),
                introduction = FormDataUtil.getBody(userInfo.introduction),
                categoryIds = null
            ).toEntity()
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    private fun handleApiException(exception: HttpException): Nothing {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

        when (errorResponse.errorCode) {
            INVALID_TOKEN -> throw ApiException.InvalidTokenException() // 만료된 토큰
            DUPLICATE_VALUE -> throw ApiException.DuplicateValueException()
            else -> throw ApiException.UnknownApiException("An unknown error occurred: ${errorResponse.message}")
        }
    }

}