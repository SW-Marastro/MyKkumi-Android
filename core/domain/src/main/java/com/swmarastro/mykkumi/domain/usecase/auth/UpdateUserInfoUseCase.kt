package com.swmarastro.mykkumi.domain.usecase.auth

import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.swmarastro.mykkumi.domain.repository.UserInfoRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
){
    suspend operator fun invoke(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO {
        return repository.updateUserInfo(userInfo)
    }
}