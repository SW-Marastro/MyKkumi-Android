package com.marastro.mykkumi.domain.usecase.post

import com.marastro.mykkumi.domain.entity.HomePostListVO
import com.marastro.mykkumi.domain.repository.PostRepository
import javax.inject.Inject

class GetHomePostListUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(cursor: String?, limit: Int?): HomePostListVO {
        return repository.getHomePostList(cursor, limit)
    }
}