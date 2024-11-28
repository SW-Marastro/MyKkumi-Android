package com.marastro.mykkumi.data.datasource

import com.marastro.mykkumi.data.dto.request.PostEditRequestDTO
import com.marastro.mykkumi.data.dto.response.HomePostListDTO
import com.marastro.mykkumi.data.dto.response.PostEditResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @DELETE("/api/v1/posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ) : Response<Void?>?
}