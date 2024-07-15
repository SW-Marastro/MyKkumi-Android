package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HelloWorldVO

interface HelloWorldRepository {
    //suspend fun getHelloWorld(): GetHelloWorldResponse
    suspend fun getHelloWorld(): HelloWorldVO
}