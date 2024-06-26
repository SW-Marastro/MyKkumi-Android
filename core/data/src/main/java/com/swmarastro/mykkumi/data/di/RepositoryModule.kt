package com.swmarastro.mykkumi.data.di

import com.swmarastro.mykkumi.data.repository.HelloWorldRepositoryImpl
import com.swmarastro.mykkumi.domain.repository.HelloWorldRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindsHelloWorldRepository(
        helloWorldRepositoryImpl: HelloWorldRepositoryImpl
    ): HelloWorldRepository
}