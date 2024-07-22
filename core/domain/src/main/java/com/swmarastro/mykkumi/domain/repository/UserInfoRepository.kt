package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.swmarastro.mykkumi.domain.entity.UserInfoVO

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfoVO

    suspend fun updateUserInfo(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO
}