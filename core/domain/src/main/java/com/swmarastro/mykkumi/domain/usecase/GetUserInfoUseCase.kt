package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.UserInfoVO
import com.swmarastro.mykkumi.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(): UserInfoVO {
        return repository.getUserInfo()
    }
}