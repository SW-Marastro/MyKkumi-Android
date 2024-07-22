package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.UserInfoVO

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfoVO
}