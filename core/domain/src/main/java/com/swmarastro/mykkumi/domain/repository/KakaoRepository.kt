package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.KakaoToken

interface KakaoRepository {
    suspend fun kakaoLogin(): KakaoToken
}