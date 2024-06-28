package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.repository.ImageRepository
import javax.inject.Inject

class LoadImageUseCase @Inject constructor(
    private val repository: ImageRepository
){

    suspend operator fun invoke(imageUrl: String):  ByteArray? {
        return repository.loadImage(imageUrl)
    }
}