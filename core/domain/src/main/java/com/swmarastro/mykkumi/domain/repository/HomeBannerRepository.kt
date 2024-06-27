package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO

interface HomeBannerRepository {
    suspend fun getHomeBanner(): HomeBannerListVO

    suspend fun getHomeBannerDetail(bannerId: Int): HomeBannerItemVO
}