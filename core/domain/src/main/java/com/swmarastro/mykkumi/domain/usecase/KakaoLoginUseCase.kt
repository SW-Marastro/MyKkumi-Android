package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.KakaoToken
import com.swmarastro.mykkumi.domain.repository.KakaoRepository
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val repository: KakaoRepository,
){
    suspend operator fun invoke() : KakaoToken {
        return repository.kakaoLogin()
    }
}