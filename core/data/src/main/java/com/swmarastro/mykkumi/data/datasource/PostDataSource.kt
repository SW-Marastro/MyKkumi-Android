package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.request.PostEditRequestDTO
import com.swmarastro.mykkumi.data.dto.response.HomePostListDTO
import com.swmarastro.mykkumi.data.dto.response.PostEditResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostDataSource {
    @GET("/api/v1/posts")
    suspend fun getHomePostList(
        @Query("cursor") cursor: String?,
        @Query("limit") limit: Int?,
    ) : HomePostListDTO

    @POST("/api/v1/posts")
    suspend fun uploadPost(
        @Body params: PostEditRequestDTO
    ) : PostEditResponseDTO
}