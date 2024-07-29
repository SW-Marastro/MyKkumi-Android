package com.swmarastro.mykkumi.data.interceptor

import com.swmarastro.mykkumi.data.datasource.ReAccessTokenDataSource
import com.swmarastro.mykkumi.data.dto.request.ReAccessTokenRequestDTO
import com.swmarastro.mykkumi.data.dto.response.ReAccessTokenResponseDTO
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authTokenDataSource: AuthTokenDataStore,
    private val reAccessTokenDataSource: Provider<ReAccessTokenDataSource>,
) : Authenticator {

    private companion object {
        private const val BEARER = "Bearer "
        private const val AUTHORIZATION = "Authorization"
    }

    // authenticate: 401 에러가 발생 될 때마다 호출됨
    override fun authenticate(route: Route?, response: Response): Request? {
        // AccessToken 만료 시 -> RefreshToken으로 AccessToken 재발급
        // RefreshToken 만료 시 -> 로그아웃 + 저장된 token 삭제

        return runBlocking {
            try {
                val accessToken = getUpdateToken().accessToken
                if(accessToken.isNotEmpty()) {
                    authTokenDataSource.saveAccessToken(accessToken)
                    response.request.newBuilder()
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .build()
                }
                else null
            }
            catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getUpdateToken() : ReAccessTokenResponseDTO {
        val refreshToken = authTokenDataSource.getRefreshToken()
        return reAccessTokenDataSource.get().getReAccessToken(
            ReAccessTokenRequestDTO(refreshToken!!)
        )
    }
}