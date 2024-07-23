package com.swmarastro.mykkumi.domain.datastore

interface AuthTokenDataStore {
    fun saveAccessToken(accessToken: String)
    fun saveRefreshToken(refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun deleteToken()
}