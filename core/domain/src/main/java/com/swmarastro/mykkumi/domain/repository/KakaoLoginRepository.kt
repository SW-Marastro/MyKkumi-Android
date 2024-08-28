package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.KakaoToken


interface KakaoLoginRepository {
    suspend fun kakaoLogin(kakaoLoginToken: KakaoToken) : Boolean
}