package com.marastro.mykkumi.domain.usecase.banner

import com.marastro.mykkumi.domain.entity.BannerListVO
import com.marastro.mykkumi.domain.repository.BannerRepository
import javax.inject.Inject

class GetBannerListUseCase @Inject constructor(
    private val repository: BannerRepository
){

    suspend operator fun invoke(): BannerListVO {
        return repository.getBannerList()
    }
}