package com.marastro.mykkumi.domain.datastore

interface AuthTokenDataStore {
    fun saveAccessToken(accessToken: String)
    fun saveRefreshToken(refreshToken: String)
    fun saveUserNickname(nickname: String)

    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getUserNickname(): String?

    fun deleteAccessToken()
    fun deleteToken()

    fun isLogin(): Boolean
}