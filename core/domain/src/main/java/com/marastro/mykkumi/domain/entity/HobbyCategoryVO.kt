package com.marastro.mykkumi.domain.entity

data class HobbyCategoryVO (
    val categories: List<HobbyCategoryItemVO>
)

data class HobbyCategoryItemVO(
    val categoryId: Long,
    val categoryName: String,
    val subCategories: List<HobbySubCategoryItemVO>
)

data class HobbySubCategoryItemVO(
    val subCategoryId: Long,
    val subCategoryName: String,
)