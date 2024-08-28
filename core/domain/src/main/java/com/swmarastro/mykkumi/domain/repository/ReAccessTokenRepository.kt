package com.swmarastro.mykkumi.domain.repository

interface ReAccessTokenRepository {
    suspend fun getReAccessToken() : Boolean
}