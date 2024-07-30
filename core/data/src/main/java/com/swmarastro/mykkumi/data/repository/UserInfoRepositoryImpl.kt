package com.swmarastro.mykkumi.data.repository

import android.content.Context
import com.google.gson.Gson
import com.swmarastro.mykkumi.data.datasource.UserInfoDataSource
import com.swmarastro.mykkumi.domain.exception.ApiException
import com.swmarastro.mykkumi.data.util.FormDataUtil
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
) :UserInfoRepository {

    private companion object {
        private const val INVALID_TOKEN = "INVALID_TOKEN"
        private const val DUPLICATE_VALUE = "DUPLICATE_VALUE"
        private const val INVALID_VALUE = "INVALID_VALUE"
    }

    //private var authorization = authTokenDataSource.getAccessToken()

    override suspend fun getUserInfo(): UserInfoVO {
        return try {
            userInfoDataSource.getUserInfo().toEntity()
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    override suspend fun updateUserInfo(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO {
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
//            params = userInfoDTO
                nickname = FormDataUtil.getBody(userInfo.nickname),
                profileImage = FormDataUtil.convertUriToMultipart(context, userInfo.profileImage),
                introduction = FormDataUtil.getBody(userInfo.introduction),
                categoryIds = null //FormDataUtil.getListLongBody(listOf<Long>(1, 2))
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
            DUPLICATE_VALUE -> throw ApiException.DuplicateValueException(errorResponse.message)
            INVALID_VALUE -> throw ApiException.InvalidNickNameValue(errorResponse.message) // 형식에 맞지 않는 닉네임
            else -> throw ApiException.UnknownApiException("An unknown error occurred: ${errorResponse.message}")
        }
    }

}