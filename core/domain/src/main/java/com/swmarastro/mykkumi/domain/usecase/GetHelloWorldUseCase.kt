package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.ApiResult
import com.swmarastro.mykkumi.domain.entity.HelloWorld
import com.swmarastro.mykkumi.domain.entity.apiResult
import com.swmarastro.mykkumi.domain.repository.HelloWorldRepository
import javax.inject.Inject

class GetHelloWorldUseCase @Inject constructor(
    private val repository: HelloWorldRepository
){
    suspend operator fun invoke(): ApiResult<HelloWorld> {
        return apiResult {
            repository.getHelloWorld()
        }
    }
}