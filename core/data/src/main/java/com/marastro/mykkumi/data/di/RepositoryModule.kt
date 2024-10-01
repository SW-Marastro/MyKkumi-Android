package com.marastro.mykkumi.data.di

import com.marastro.mykkumi.data.repository.BannerRepositoryImpl
import com.marastro.mykkumi.data.repository.HobbyCategoryRepositoryImpl
import com.marastro.mykkumi.data.repository.KakaoLoginRepositoryImpl
import com.marastro.mykkumi.data.repository.PostRepositoryImpl
import com.marastro.mykkumi.data.repository.PreSignedUrlRepositoryImpl
import com.marastro.mykkumi.data.repository.ReAccessTokenRepositoryImpl
import com.marastro.mykkumi.data.repository.ReportRepositoryImpl
import com.marastro.mykkumi.data.repository.UserInfoRepositoryImpl
import com.marastro.mykkumi.domain.repository.BannerRepository
import com.marastro.mykkumi.domain.repository.HobbyCategoryRepository
import com.marastro.mykkumi.domain.repository.KakaoLoginRepository
import com.marastro.mykkumi.domain.repository.PostRepository
import com.marastro.mykkumi.domain.repository.PreSignedUrlRepository
import com.marastro.mykkumi.domain.repository.ReAccessTokenRepository
import com.marastro.mykkumi.domain.repository.ReportRepository
import com.marastro.mykkumi.domain.repository.UserInfoRepository
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

    @Binds
    @Singleton
    abstract fun bindsKakaoLoginRepository(
        kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl
    ): KakaoLoginRepository


    @Binds
    @Singleton
    abstract fun bindsUserInfoRepository(
        userInfoRepositoryImpl: UserInfoRepositoryImpl
    ): UserInfoRepository

    @Binds
    @Singleton
    abstract fun bindsReAccessTokenRepository(
        reAccessTokenRepositoryImpl: ReAccessTokenRepositoryImpl
    ): ReAccessTokenRepository

    @Binds
    @Singleton
    abstract fun bindsHobbyCategoryRepository(
        hobbyCategoryRepositoryImpl: HobbyCategoryRepositoryImpl
    ): HobbyCategoryRepository

    @Binds
    @Singleton
    abstract fun bindsPreSignedUrlRepository(
        preSignedUrlRepositoryImpl: PreSignedUrlRepositoryImpl
    ): PreSignedUrlRepository

    @Binds
    @Singleton
    abstract fun bindsReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository
}