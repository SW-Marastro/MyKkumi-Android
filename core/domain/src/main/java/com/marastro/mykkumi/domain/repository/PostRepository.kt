package com.marastro.mykkumi.domain.repository

import com.marastro.mykkumi.domain.entity.HomePostListVO
import com.marastro.mykkumi.domain.entity.PostEditResponseVO
import com.marastro.mykkumi.domain.entity.PostImageVO

interface PostRepository {
    suspend fun getHomePostList(cursor: String?, limit: Int?): HomePostListVO
    suspend fun uploadPost(subCategory: Long, content: String?, postImages: MutableList<PostImageVO>): PostEditResponseVO
    suspend fun deletePost(postId: Int) : Boolean
}