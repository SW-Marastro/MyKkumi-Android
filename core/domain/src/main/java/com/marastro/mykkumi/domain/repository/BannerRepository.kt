package com.marastro.mykkumi.domain.repository

import com.marastro.mykkumi.domain.entity.BannerDetailVO
import com.marastro.mykkumi.domain.entity.BannerListVO

interface BannerRepository {
    suspend fun getBannerList(): BannerListVO // HomeBannerListResponse

    suspend fun getBannerDetail(bannerId: Int): BannerDetailVO
}