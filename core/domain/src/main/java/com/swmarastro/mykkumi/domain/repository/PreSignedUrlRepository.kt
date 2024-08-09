package com.swmarastro.mykkumi.domain.repository

interface PreSignedUrlRepository {
    suspend fun getPreSignedUrl(): String
}