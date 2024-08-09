package com.swmarastro.mykkumi.domain.repository

interface PreSignedUrlRepository {
    suspend fun getPreSignedUrl(imageLocalUri: Any): String?
}