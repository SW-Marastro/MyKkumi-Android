package com.marastro.mykkumi.domain.repository

import com.marastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.marastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.marastro.mykkumi.domain.entity.UserInfoVO

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfoVO

    suspend fun updateUserInfo(userInfo: UpdateUserInfoRequestVO): UpdateUserInfoResponseVO
}