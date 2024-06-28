package com.swmarastro.mykkumi.data.repository

import android.util.Log
import com.swmarastro.mykkumi.data.datasource.HomeBannerDataSource
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerListResponse
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.repository.HomeBannerRepository
import javax.inject.Inject

class HomeBannerRepositoryImpl @Inject constructor(
    private val homeBannerDataSource: HomeBannerDataSource
) : HomeBannerRepository {

    override suspend fun getHomeBanner(): HomeBannerListVO {
        return homeBannerDataSource.getHomeBanners().toEntity()
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

    override suspend fun getHomeBannerDetail(bannerId: Int): HomeBannerItemVO {
        return homeBannerDataSource.getHomeBannerDetail(bannerId).toEntity()
    }
}