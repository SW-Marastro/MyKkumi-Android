package com.swmarastro.mykkumi.data.di

import android.content.Context
import com.swmarastro.mykkumi.data.datastore.AuthTokenDataStoreImpl
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideAuthTokenDataStore(@ApplicationContext context: Context): AuthTokenDataStore {
        return AuthTokenDataStoreImpl(context)
    }
}