package com.swmarastro.mykkumi.domain.usecase

import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.repository.PostRepository
import javax.inject.Inject

class GetHomePostListUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(cursor: String?, limit: Int?): HomePostListVO {
        return repository.getHomePostList(cursor, limit)
    }
}