package com.swmarastro.mykkumi.data.datasource

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface PutImageS3DataSource {

    @PUT
    suspend fun putImageForS3(
        @Url url: String, // preSigned url
        @Body image: RequestBody
    )
}