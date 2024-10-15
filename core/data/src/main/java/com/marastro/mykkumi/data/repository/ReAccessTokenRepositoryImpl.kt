package com.marastro.mykkumi.data.repository

import android.util.Log
import com.google.gson.Gson
import com.marastro.mykkumi.data.datasource.ReAccessTokenDataSource
import com.marastro.mykkumi.data.dto.request.ReAccessTokenRequestDTO
import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.marastro.mykkumi.domain.exception.ApiException
import com.marastro.mykkumi.domain.exception.ErrorResponse
import com.marastro.mykkumi.domain.repository.ReAccessTokenRepository
import retrofit2.HttpException
import javax.inject.Inject

class ReAccessTokenRepositoryImpl @Inject constructor(
    private val reAccessTokenDataSource: ReAccessTokenDataSource,
    private val authTokenDataSource: AuthTokenDataStore,
) : ReAccessTokenRepository {

    private companion object {
        private const val INVALID_TOKEN = "INVALID_TOKEN"
    }

    private fun getAuthorization(): String {
        return authTokenDataSource.getRefreshToken() ?: throw ApiException.InvalidTokenException()
    }

    override suspend fun getReAccessToken(): Boolean {
        val authorization = getAuthorization()

        try {
            // refresh Token으로 Access Token 재발급
            val response = reAccessTokenDataSource.getReAccessToken(
                ReAccessTokenRequestDTO(authorization)
            )
            authTokenDataSource.saveAccessToken(response.accessToken)

            return true
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    private fun handleApiException(exception: HttpException): Nothing {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

        when (errorResponse.errorCode) {
            INVALID_TOKEN -> { // 토큰 삭제 - 로그아웃
                Log.d("test", "delete token ------------------")
                authTokenDataSource.deleteToken()
                throw ApiException.InvalidRefreshTokenException()
            }
            else -> throw ApiException.UnknownApiException("An unknown error occurred: ${errorResponse.message}")
        }
    }
}