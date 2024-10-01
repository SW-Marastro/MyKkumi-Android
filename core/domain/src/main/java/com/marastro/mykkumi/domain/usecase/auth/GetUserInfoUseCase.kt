package com.marastro.mykkumi.domain.usecase.auth

import com.marastro.mykkumi.domain.entity.UserInfoVO
import com.marastro.mykkumi.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(): UserInfoVO {
        return repository.getUserInfo()
    }
}