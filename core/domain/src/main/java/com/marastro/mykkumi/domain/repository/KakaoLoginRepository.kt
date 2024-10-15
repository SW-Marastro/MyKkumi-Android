package com.marastro.mykkumi.domain.repository

import com.marastro.mykkumi.domain.entity.KakaoToken


interface KakaoLoginRepository {
    suspend fun kakaoLogin(kakaoLoginToken: KakaoToken) : Boolean
}