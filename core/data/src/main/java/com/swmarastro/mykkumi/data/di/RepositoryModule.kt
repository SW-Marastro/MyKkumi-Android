package com.swmarastro.mykkumi.data.di

import com.swmarastro.mykkumi.data.repository.BannerRepositoryImpl
import com.swmarastro.mykkumi.data.repository.PostRepositoryImpl
import com.swmarastro.mykkumi.domain.repository.BannerRepository
import com.swmarastro.mykkumi.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsBannerRepository(
        bannerRepositoryImpl: BannerRepositoryImpl
    ): BannerRepository

    @Binds
    @Singleton
    abstract fun bindsPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository
}