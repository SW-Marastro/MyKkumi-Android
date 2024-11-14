package com.marastro.mykkumi.feature.post.hobbyCategory

import androidx.lifecycle.LiveData

interface AccessAbleToHobbyCategory {
    val selectHobbyCategory : LiveData<Long>

    fun setHobbySelected(subCategoryId: Long)
}