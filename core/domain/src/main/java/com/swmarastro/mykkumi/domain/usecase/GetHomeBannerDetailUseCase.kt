package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO
import com.swmarastro.mykkumi.domain.repository.HomeBannerRepository
import javax.inject.Inject

class GetHomeBannerDetailUseCase @Inject constructor(
    private val repository: HomeBannerRepository
){

    suspend operator fun invoke(bannerId: Int): HomeBannerItemVO {
        return repository.getHomeBannerDetail(bannerId)
    }
}