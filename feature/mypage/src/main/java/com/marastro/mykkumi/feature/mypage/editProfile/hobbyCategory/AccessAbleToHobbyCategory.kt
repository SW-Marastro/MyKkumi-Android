package com.marastro.mykkumi.feature.mypage.editProfile.hobbyCategory

import androidx.lifecycle.LiveData

interface AccessAbleToHobbyCategory {
    val selectHobbyCategories : LiveData<MutableSet<Long>>

    fun setHobbySelected(subCategoryId: Long)
}