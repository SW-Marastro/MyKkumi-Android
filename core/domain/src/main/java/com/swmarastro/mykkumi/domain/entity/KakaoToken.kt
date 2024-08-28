package com.swmarastro.mykkumi.domain.entity

import java.util.Date

data class KakaoToken(
    val accessToken: String = "",
//    val accessTokenExpiresAt: Date = Date(),
    val refreshToken: String = "",
//    val refreshTokenExpiresAt: Date = Date(),
//    val idToken: String? = null,
//    val scopes: List<String>? = null
)