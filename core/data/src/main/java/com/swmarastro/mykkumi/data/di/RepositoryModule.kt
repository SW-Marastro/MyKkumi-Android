package com.swmarastro.mykkumi.data.di

import com.swmarastro.mykkumi.data.repository.BannerRepositoryImpl
import com.swmarastro.mykkumi.data.repository.HobbyCategoryRepositoryImpl
import com.swmarastro.mykkumi.data.repository.KakaoLoginRepositoryImpl
import com.swmarastro.mykkumi.data.repository.PostRepositoryImpl
import com.swmarastro.mykkumi.data.repository.PreSignedUrlRepositoryImpl
import com.swmarastro.mykkumi.data.repository.ReAccessTokenRepositoryImpl
import com.swmarastro.mykkumi.data.repository.ReportRepositoryImpl
import com.swmarastro.mykkumi.data.repository.UserInfoRepositoryImpl
import com.swmarastro.mykkumi.domain.repository.BannerRepository
import com.swmarastro.mykkumi.domain.repository.HobbyCategoryRepository
import com.swmarastro.mykkumi.domain.repository.KakaoLoginRepository
import com.swmarastro.mykkumi.domain.repository.PostRepository
import com.swmarastro.mykkumi.domain.repository.PreSignedUrlRepository
import com.swmarastro.mykkumi.domain.repository.ReAccessTokenRepository
import com.swmarastro.mykkumi.domain.repository.ReportRepository
import com.swmarastro.mykkumi.domain.repository.UserInfoRepository
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