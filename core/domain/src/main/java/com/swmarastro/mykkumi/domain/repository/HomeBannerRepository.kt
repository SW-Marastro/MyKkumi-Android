package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerListResponse

interface HomeBannerRepository {
    suspend fun getHomeBanner(): HomeBannerListVO // HomeBannerListResponse

    suspend fun getHomeBannerDetail(bannerId: Int): HomeBannerItemVO
}