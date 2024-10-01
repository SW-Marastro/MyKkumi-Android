package com.marastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.marastro.mykkumi.domain.entity.HobbyCategoryItemVO
import com.marastro.mykkumi.domain.entity.HobbyCategoryVO
import com.marastro.mykkumi.domain.entity.HobbySubCategoryItemVO

data class HobbyCategoryDTO(
    @SerializedName("categories")
    val categories: List<HobbyCategoryItemDTO>
) {
    fun toEntity(): HobbyCategoryVO = HobbyCategoryVO(
        categories = categories.map { it.toEntity() }
    )

    data class HobbyCategoryItemDTO(
        @SerializedName("id")
        val id: Long,

        @SerializedName("name")
        val name: String,

        @SerializedName("subCategories")
        val subCategories: List<HobbySubCategoryItemDTO>
    ) {
        fun toEntity(): HobbyCategoryItemVO = HobbyCategoryItemVO(
            categoryId = id,
            categoryName = name,
            subCategories = subCategories.map { it.toEntity() }
        )

        data class HobbySubCategoryItemDTO(
            @SerializedName("id")
            val id: Long,

            @SerializedName("name")
            val name: String,
        ) {
            fun toEntity(): HobbySubCategoryItemVO = HobbySubCategoryItemVO(
                subCategoryId = id,
                subCategoryName = name
            )
        }
    }
}

