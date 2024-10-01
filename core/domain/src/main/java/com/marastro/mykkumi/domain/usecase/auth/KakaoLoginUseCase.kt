package com.marastro.mykkumi.domain.usecase.auth

import com.marastro.mykkumi.domain.entity.KakaoToken
import com.marastro.mykkumi.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val repository: KakaoLoginRepository,
){
    suspend operator fun invoke(kakaoLoginToken: KakaoToken) : Boolean {
        return repository.kakaoLogin(kakaoLoginToken)
    }
}