package com.marastro.mykkumi.data.repository

import com.google.gson.Gson
import com.marastro.mykkumi.data.datasource.PostDataSource
import com.marastro.mykkumi.data.dto.request.PostEditRequestDTO
import com.marastro.mykkumi.data.dto.request.PostImageRequestDTO
import com.marastro.mykkumi.data.dto.request.PostPinRequestDTO
import com.marastro.mykkumi.data.dto.request.PostProductRequestDTO
import com.marastro.mykkumi.domain.entity.HomePostListVO
import com.marastro.mykkumi.domain.entity.PostEditResponseVO
import com.marastro.mykkumi.domain.entity.PostImageVO
import com.marastro.mykkumi.domain.exception.ApiException
import com.marastro.mykkumi.domain.exception.ErrorResponse
import com.marastro.mykkumi.domain.repository.PostRepository
import retrofit2.HttpException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
) : PostRepository {

    private companion object {
        private const val INVALID_TOKEN = "INVALID_TOKEN"
        private const val INVALID_VALUE = "INVALID_VALUE"
        private const val ACCESS_DENIED = "ACCESS_DENIED"
    }

    override suspend fun getHomePostList(cursor: String?, limit: Int?): HomePostListVO {
        return postDataSource.getHomePostList(cursor, limit).toEntity()
    }

    override suspend fun uploadPost(subCategory: Long, content: String?, postImages: MutableList<PostImageVO>): PostEditResponseVO {

        return postDataSource.uploadPost(
            PostEditRequestDTO(
                subCategoryId= subCategory,
                content = content,
                images = postImages.map { image ->
                    PostImageRequestDTO(
                        url = image.imageUri,
                        pins = image.pinList.map { pin ->
                            PostPinRequestDTO(
                                positionX = pin.positionX.toDouble(),
                                positionY = pin.positionY.toDouble(),
                                productInfo = PostProductRequestDTO(
                                    name = pin.product.productName,
                                    url = pin.product.productUrl
                                )
                            )
                        }.toList()
                    )
                }.toList()
            )
        ).toEntity()
    }

    override suspend fun deletePost(postId: Int) : Boolean {
        try {
            postDataSource.deletePost(postId)

            return true
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    private fun handleApiException(exception: HttpException): Nothing {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

        when (errorResponse.errorCode) {
            INVALID_TOKEN -> throw ApiException.InvalidTokenException() // 만료된 토큰
            INVALID_VALUE -> throw ApiException.InvalidPostValue(errorResponse.message) // 존재하지 않는 게시물
            ACCESS_DENIED -> throw ApiException.AccessDeniedUserForPost(errorResponse.message) // 삭제 권한 없음
            else -> throw ApiException.UnknownApiException("An unknown error occurred: ${errorResponse.message}")
        }
    }
}