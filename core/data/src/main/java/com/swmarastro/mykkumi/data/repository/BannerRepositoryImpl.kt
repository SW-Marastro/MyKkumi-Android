package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.BannerDataSource
import com.swmarastro.mykkumi.domain.entity.BannerDetailVO
import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.repository.BannerRepository
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