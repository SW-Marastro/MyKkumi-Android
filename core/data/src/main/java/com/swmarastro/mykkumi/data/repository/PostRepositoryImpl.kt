package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.PostDataSource
import com.swmarastro.mykkumi.data.dto.request.PostEditRequestDTO
import com.swmarastro.mykkumi.data.dto.request.PostImageRequestDTO
import com.swmarastro.mykkumi.data.dto.request.PostPinRequestDTO
import com.swmarastro.mykkumi.data.dto.request.PostProductRequestDTO
import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.entity.PostEditResponseVO
import com.swmarastro.mykkumi.domain.entity.PostImageVO
import com.swmarastro.mykkumi.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
) : PostRepository {

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
}