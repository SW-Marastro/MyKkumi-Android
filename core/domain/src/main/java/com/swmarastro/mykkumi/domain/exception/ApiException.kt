package com.swmarastro.mykkumi.domain.exception

sealed class ApiException(message: String) : Exception(message) {
    class InvalidTokenException : ApiException("Invalid token. Please log in again.")
    class DuplicateValueException : ApiException("중복된 닉네임입니다.")
    class InvalidRefreshTokenException : ApiException("로그아웃 되었습니다")
    class UnknownApiException(message: String) : ApiException(message)
}