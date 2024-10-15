package com.marastro.mykkumi.domain.usecase.auth

import com.marastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.marastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.marastro.mykkumi.domain.repository.UserInfoRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
){
    suspend operator fun invoke(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO {
        return repository.updateUserInfo(userInfo)
    }
}