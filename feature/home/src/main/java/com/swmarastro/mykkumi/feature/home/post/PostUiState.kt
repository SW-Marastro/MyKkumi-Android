package com.swmarastro.mykkumi.feature.home.post

import com.swmarastro.mykkumi.domain.entity.HomePostItemVO

data class PostUiState (
    val postList: MutableList<HomePostItemVO>,
    var postLimit: Int? = null,
    var postCursor: String? = null,
    var isPostEnd: Boolean = false
)