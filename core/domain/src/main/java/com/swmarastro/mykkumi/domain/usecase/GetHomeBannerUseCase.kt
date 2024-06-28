package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.repository.HomeBannerRepository
import javax.inject.Inject

class GetHomeBannerUseCase @Inject constructor(
    private val repository: HomeBannerRepository
){

    suspend operator fun invoke(): HomeBannerListVO {
        return repository.getHomeBanner()
    }
}