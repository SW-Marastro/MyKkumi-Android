package com.swmarastro.mykkumi.data.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class AuthTokenDataSourceImpl (context: Context) : AuthTokenDataSource {

    private val BEARER = "Bearer "

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString("accessToken", BEARER + accessToken).apply()
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString("refreshToken", BEARER + refreshToken).apply()
    }

    override fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }
}