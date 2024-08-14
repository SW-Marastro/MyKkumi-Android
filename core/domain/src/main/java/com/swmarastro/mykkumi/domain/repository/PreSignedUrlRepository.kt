package com.swmarastro.mykkumi.domain.repository

interface PreSignedUrlRepository {
    suspend fun getPreSignedPostUrl(imageLocalUri: Any): String?
    suspend fun getPreSignedProfileUrl(imageLocalUri: Any): String?
}