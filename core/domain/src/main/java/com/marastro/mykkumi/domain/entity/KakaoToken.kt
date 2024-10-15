package com.marastro.mykkumi.domain.entity

data class KakaoToken(
    val accessToken: String = "",
//    val accessTokenExpiresAt: Date = Date(),
    val refreshToken: String = "",
//    val refreshTokenExpiresAt: Date = Date(),
//    val idToken: String? = null,
//    val scopes: List<String>? = null
)