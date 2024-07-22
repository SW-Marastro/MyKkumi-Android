package com.swmarastro.mykkumi.domain.entity

data class UserInfoVO (
    val nickname: String?,
    val email: String?,
    val introduction: String?,
    val profileImage: String?,
)

data class UpdateUserInfoRequestVO(
    val nickname: String?,
    val profileImage: Any?, // Uri
    val introduction: String?,
    val categoryId: List<Long>?
)

data class UpdateUserInfoResponseVO(
    val nickname: String?,
    val profileImage: String?,
    val introduction: String?,
    val categoryId: List<Long>?
)