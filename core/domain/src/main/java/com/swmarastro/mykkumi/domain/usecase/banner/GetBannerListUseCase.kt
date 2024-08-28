package com.swmarastro.mykkumi.domain.usecase.banner

import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.repository.BannerRepository
import javax.inject.Inject

class GetBannerListUseCase @Inject constructor(
    private val repository: BannerRepository
){

    suspend operator fun invoke(): BannerListVO {
        return repository.getBannerList()
    }
}