package com.marastro.mykkumi.data.repository

import com.marastro.mykkumi.data.datasource.BannerDataSource
import com.marastro.mykkumi.domain.entity.BannerDetailVO
import com.marastro.mykkumi.domain.entity.BannerListVO
import com.marastro.mykkumi.domain.repository.BannerRepository
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerDataSource: BannerDataSource,
) : BannerRepository {

    override suspend fun getBannerList(): BannerListVO {
        return bannerDataSource.getBannerList().toEntity()
    }

    /*
    override suspend fun getHomeBanner(): HomeBannerListResponse {
        val response = homeBannerDataSource.getHomeBanners()
        return HomeBannerListResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }
    */

    override suspend fun getBannerDetail(bannerId: Int): BannerDetailVO {
        return bannerDataSource.getBannerDetail(bannerId).toEntity()
    }
}