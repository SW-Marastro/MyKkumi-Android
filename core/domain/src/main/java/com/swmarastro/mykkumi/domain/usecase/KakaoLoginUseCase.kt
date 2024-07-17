package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.KakaoToken
import com.swmarastro.mykkumi.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val repository: KakaoLoginRepository,
){
    suspend operator fun invoke(kakaoLoginToken: KakaoToken) : Boolean {
        return repository.kakaoLogin(kakaoLoginToken)
    }
}