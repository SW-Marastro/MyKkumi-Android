package com.swmarastro.mykkumi.domain.repository

interface ImageRepository {
    suspend fun loadImage(imageUrl: String): ByteArray?
}