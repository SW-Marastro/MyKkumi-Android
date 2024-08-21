package com.swmarastro.mykkumi.domain.exception

sealed class ApiException(message: String) : Exception(message) {
    class InvalidTokenException : ApiException("Invalid token. Please log in again.")
    class DuplicateValueException(message: String) : ApiException(message)
    class InvalidNickNameValue(message: String): ApiException(message)
    class InvalidRefreshTokenException : ApiException("장시간 접속하지 않아 로그아웃 되었습니다.")
    class UnknownApiException(message: String) : ApiException(message)
}