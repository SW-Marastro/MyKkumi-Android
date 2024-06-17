package com.swmarastro.mykkumi.domain.entity

class GetHelloWorldResponse : BaseResponse<HelloWorld>()
data class HelloWorld(
    val title: String
)