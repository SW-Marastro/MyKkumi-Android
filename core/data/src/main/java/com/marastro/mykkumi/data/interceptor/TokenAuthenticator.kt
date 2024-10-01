package com.marastro.mykkumi.data.interceptor

import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.marastro.mykkumi.domain.repository.ReAccessTokenRepository
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
        private const val MAX_RETRY_COUNT = 5
    }

    // authenticate: 401 에러가 발생 될 때마다 호출됨
    override fun authenticate(route: Route?, response: Response): Request? {
        // AccessToken 만료 시 -> RefreshToken으로 AccessToken 재발급
        // RefreshToken 만료 시 -> 로그아웃 + 저장된 token 삭제

        // 연속 호출 횟수 제한 - RefreshToken까지 만료되었을 경우 무한 호출에 빠질 가능성이 있음
        if (responseCount(response) >= MAX_RETRY_COUNT) {
            authTokenDataSource.deleteToken()
        }

        return runBlocking {
            try {
                val isSuccess = reAccessTokenRepository.get().getReAccessToken()

                if(isSuccess) { // AccessToken 재발급 성공
                    val accessToken = authTokenDataSource.getAccessToken()

                    // 401 에러가 발생됐던 api를 다시 호출
                    response.request.newBuilder()
                        .header(AUTHORIZATION, accessToken!!)
                        .build()
                }
                else null // AccessToken 재발급 실패 -> 처리는 Repository에서 하고 옴
            }
            catch (e: Exception) {
                null
            }
        }
    }

    // 지금 호출된 횟수가 몇 번인지 체크
    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }
}