package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.HomeBannerDataSource
import com.swmarastro.mykkumi.domain.repository.HomeBannerRepository
import javax.inject.Inject

class HomeBannerRepository @Inject constructor(
    private val homeBannerDataSource: HomeBannerDataSource
) : HomeBannerRepository {
}