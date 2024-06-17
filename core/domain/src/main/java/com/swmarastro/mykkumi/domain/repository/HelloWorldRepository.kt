package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.GetHelloWorldResponse

interface HelloWorldRepository {
    suspend fun getHelloWorld(): GetHelloWorldResponse
}