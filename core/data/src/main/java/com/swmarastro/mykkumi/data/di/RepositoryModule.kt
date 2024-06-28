package com.swmarastro.mykkumi.data.di

import com.swmarastro.mykkumi.data.repository.HomeBannerRepositoryImpl
import com.swmarastro.mykkumi.data.repository.ImageRepositoryImpl
import com.swmarastro.mykkumi.domain.repository.HomeBannerRepository
import com.swmarastro.mykkumi.domain.repository.ImageRepository
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
    abstract fun bindsImageRepository(
        imageRepositoryImpl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    @Singleton
    abstract fun bindsHomeBannerRepository(
        homeBannerRepositoryImpl: HomeBannerRepositoryImpl
    ): HomeBannerRepository
}