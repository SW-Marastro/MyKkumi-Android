package com.swmarastro.mykkumi.data.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthTokenDataStore {

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    override fun getAccessToken(): String? {
        return BEARER + sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    override fun deleteToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN)
        sharedPreferences.edit().remove(REFRESH_TOKEN)
    }

    private companion object {
        private const val BEARER = "Bearer "

        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}