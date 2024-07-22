package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.UserInfoDataSource
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.domain.entity.UserInfoVO
import com.swmarastro.mykkumi.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource,
    private val authTokenDataSource: AuthTokenDataStore,
) :UserInfoRepository {
    override suspend fun getUserInfo(): UserInfoVO {
        var authorization = authTokenDataSource.getAccessToken()
        if(authorization == null) authorization = "" // 에러 처리 필요
        return userInfoDataSource.getUserInfo(authorization).toEntity()
    }
}