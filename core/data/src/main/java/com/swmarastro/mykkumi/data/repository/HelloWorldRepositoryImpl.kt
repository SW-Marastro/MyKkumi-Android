package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.HelloWorldDataSource
import javax.inject.Inject

class HelloWorldRepositoryImpl @Inject constructor(
    private val helloWorldDataSource: HelloWorldDataSource
){

    // override suspend fun getHelloWorld()
}