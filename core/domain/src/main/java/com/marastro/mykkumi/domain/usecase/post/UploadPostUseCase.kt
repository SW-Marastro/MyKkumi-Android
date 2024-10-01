package com.marastro.mykkumi.domain.usecase.post

import com.marastro.mykkumi.domain.entity.PostEditResponseVO
import com.marastro.mykkumi.domain.entity.PostImageVO
import com.marastro.mykkumi.domain.repository.PostRepository
import javax.inject.Inject

class UploadPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(subCategory: Long, content: String?, postImages: MutableList<PostImageVO>): PostEditResponseVO {
        return repository.uploadPost(subCategory, content, postImages)
    }
}