package com.swmarastro.mykkumi.data.datasource

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface PutImageS3DataSource {

    @Multipart
    @PUT
    suspend fun putImageForS3(
        @Url url: String, // preSigned url
        @Body params: MultipartBody.Part,
    )
}