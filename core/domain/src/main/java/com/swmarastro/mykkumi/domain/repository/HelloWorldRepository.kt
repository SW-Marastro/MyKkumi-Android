package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.GetHelloWorldResponse
import com.swmarastro.mykkumi.domain.entity.HelloWorld

interface HelloWorldRepository {
    //suspend fun getHelloWorld(): GetHelloWorldResponse
    suspend fun getHelloWorld(): HelloWorld
}