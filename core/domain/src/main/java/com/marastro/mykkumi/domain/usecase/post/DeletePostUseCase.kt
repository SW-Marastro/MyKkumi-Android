package com.marastro.mykkumi.domain.usecase.post

import com.marastro.mykkumi.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Boolean {
        return repository.deletePost(postId)
    }
}