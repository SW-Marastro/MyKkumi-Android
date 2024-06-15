package com.swmarastro.mykkumi.domain.repository

interface HelloWorldRepository {
    fun getHelloWorld(): Result<Unit>
}