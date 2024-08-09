package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.PreSignedUrlDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface PreSignedUrlDataSource {
    @GET("/api/v1/posts/preSignedUrl")
    suspend fun getPreSignedUrl(
        @Query("extension") extension: String = "jpeg" // 확장자
    ) : PreSignedUrlDTO
}