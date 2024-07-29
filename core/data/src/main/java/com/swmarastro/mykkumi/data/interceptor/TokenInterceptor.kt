package com.swmarastro.mykkumi.data.interceptor

import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.domain.exception.ApiException
import com.swmarastro.mykkumi.domain.repository.ReAccessTokenRepository
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
    private val reAccessTokenRepository: Provider<ReAccessTokenRepository>,
) : Authenticator {

    private companion object {
        private const val AUTHORIZATION = "Authorization"
    }

    // authenticate: 401 에러가 발생 될 때마다 호출됨
    override fun authenticate(route: Route?, response: Response): Request? {
        // AccessToken 만료 시 -> RefreshToken으로 AccessToken 재발급
        // RefreshToken 만료 시 -> 로그아웃 + 저장된 token 삭제
        return runBlocking {
            when (reAccessTokenRepository.get().getReAccessToken()) {
                true -> {
                    val accessToken = authTokenDataSource.getAccessToken() ?: throw ApiException.InvalidTokenException()
                    response.request.newBuilder()
                        .header(AUTHORIZATION, accessToken)
                        .build()
                }
                else -> null
            }
        }
    }
}