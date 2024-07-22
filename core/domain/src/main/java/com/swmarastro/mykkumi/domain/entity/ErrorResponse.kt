package com.swmarastro.mykkumi.domain.entity

data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val detail: String
)
