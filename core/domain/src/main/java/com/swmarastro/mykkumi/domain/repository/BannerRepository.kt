package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.entity.BannerItemVO

interface BannerRepository {
    suspend fun getBannerList(): BannerListVO // HomeBannerListResponse

    suspend fun getBannerDetail(bannerId: Int): BannerItemVO
}