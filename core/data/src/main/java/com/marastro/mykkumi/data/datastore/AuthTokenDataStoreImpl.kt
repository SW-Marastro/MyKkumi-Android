package com.marastro.mykkumi.data.datastore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthTokenDataStore {

    private val sharedPreferences: SharedPreferences = try {
        EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        Log.e("AuthTokenDataStoreImpl", "Error initializing EncryptedSharedPreferences, falling back to default SharedPreferences", e)
        context.getSharedPreferences("fallback_prefs", Context.MODE_PRIVATE)
    }

    override fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    // TODO: uuid로 변경
    override fun saveUserNickname(nickname: String) {
        sharedPreferences.edit().putString(USER_NICKNAME, nickname).apply()
    }


    override fun getAccessToken(): String? {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN, null)

        if (accessToken.isNullOrEmpty()) return null
        return BEARER + accessToken
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    // TODO: uuid로 변경
    override fun getUserNickname(): String? {
        return sharedPreferences.getString(USER_NICKNAME, null)
    }


    override fun deleteAccessToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
    }

    override fun deleteToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
        sharedPreferences.edit().remove(REFRESH_TOKEN).apply()

        sharedPreferences.edit().remove(USER_NICKNAME).apply() // TODO: uuid로 변경
    }

    override fun isLogin(): Boolean { // 로그인 유무 = Token 존재 유무
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN, null)
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN, null)

        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }

    private companion object {
        private const val BEARER = "Bearer "

        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_NICKNAME = "nickname"
    }
}