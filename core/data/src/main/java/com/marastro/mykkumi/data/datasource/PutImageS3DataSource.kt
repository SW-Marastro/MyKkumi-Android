package com.marastro.mykkumi.data.datasource

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

interface PutImageS3DataSource {

    @PUT
    suspend fun putImageForS3(
        @Url url: String, // preSigned url
        @Body image: RequestBody
    )
}