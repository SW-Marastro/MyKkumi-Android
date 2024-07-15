package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.PostDataSource
import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
) : PostRepository {

    override suspend fun getHomePostList(cursor: String?, limit: Int?): HomePostListVO {
        return postDataSource.getHomePostList(cursor, limit).toEntity()
    }
}