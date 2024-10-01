package com.marastro.mykkumi.domain.usecase.banner

import com.marastro.mykkumi.domain.entity.BannerDetailVO
import com.marastro.mykkumi.domain.repository.BannerRepository
import javax.inject.Inject

class GetBannerDetailUseCase @Inject constructor(
    private val repository: BannerRepository
){

    suspend operator fun invoke(bannerId: Int): BannerDetailVO {
        return repository.getBannerDetail(bannerId)
    }
}