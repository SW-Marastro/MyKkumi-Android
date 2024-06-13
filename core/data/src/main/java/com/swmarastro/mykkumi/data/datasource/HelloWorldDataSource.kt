package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.HelloWorldResponse
import retrofit2.http.GET

interface HelloWorldDataSource {
    @GET("v1/home")
    suspend fun getHelloWorld(
    ) : Result<HelloWorldResponse>
}