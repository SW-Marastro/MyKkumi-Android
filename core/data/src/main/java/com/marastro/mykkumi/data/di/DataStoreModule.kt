package com.marastro.mykkumi.data.di

import android.content.Context
import com.marastro.mykkumi.data.datastore.AuthTokenDataStoreImpl
import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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