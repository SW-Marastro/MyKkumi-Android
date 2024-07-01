package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import com.swmarastro.mykkumi.domain.repository.BannerRepository
import javax.inject.Inject

class GetBannerDetailUseCase @Inject constructor(
    private val repository: BannerRepository
){

    suspend operator fun invoke(bannerId: Int): BannerItemVO {
        return repository.getBannerDetail(bannerId)
    }
}