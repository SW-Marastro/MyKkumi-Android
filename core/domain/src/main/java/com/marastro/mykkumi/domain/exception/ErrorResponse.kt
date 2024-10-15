package com.marastro.mykkumi.domain.exception

data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val detail: String
)
