package com.marastro.mykkumi.domain.repository

interface ReAccessTokenRepository {
    suspend fun getReAccessToken() : Boolean
}