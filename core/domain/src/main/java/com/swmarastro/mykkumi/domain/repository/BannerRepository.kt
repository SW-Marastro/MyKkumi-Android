package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.BannerDetailVO
import com.swmarastro.mykkumi.domain.entity.BannerListVO

interface BannerRepository {
    suspend fun getBannerList(): BannerListVO // HomeBannerListResponse

    suspend fun getBannerDetail(bannerId: Int): BannerDetailVO
}