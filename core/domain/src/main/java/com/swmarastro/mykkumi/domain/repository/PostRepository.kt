package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.entity.PostEditResponseVO
import com.swmarastro.mykkumi.domain.entity.PostImageVO

interface PostRepository {
    suspend fun getHomePostList(cursor: String?, limit: Int?): HomePostListVO
    suspend fun uploadPost(subCategory: Long, content: String?, postImages: MutableList<PostImageVO>): PostEditResponseVO
}