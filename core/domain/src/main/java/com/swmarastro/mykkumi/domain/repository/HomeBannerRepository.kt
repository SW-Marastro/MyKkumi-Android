package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO

interface HomeBannerRepository {
    suspend fun getHomeBanner(): HomeBannerListVO
}