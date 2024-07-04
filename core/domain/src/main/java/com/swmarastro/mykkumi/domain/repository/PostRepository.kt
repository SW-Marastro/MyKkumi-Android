package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HomePostListVO

interface PostRepository {

    suspend fun getHomePostList(cursor: String?, limit: Int?): HomePostListVO
}