package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.HelloWorldResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface HelloWorldDataSource {
    /*@GET("v1/home")
    suspend fun getHelloWorld(
    ) : Response<HelloWorldResult>*/
    @GET("v1/home")
    suspend fun getHelloWorld(
    ) : HelloWorldResult
}