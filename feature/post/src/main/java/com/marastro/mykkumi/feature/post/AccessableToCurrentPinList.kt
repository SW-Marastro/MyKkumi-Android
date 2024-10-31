package com.marastro.mykkumi.feature.post

import androidx.lifecycle.LiveData
import com.marastro.mykkumi.domain.entity.PostEditPinVO

interface AccessableToCurrentPinList {
    val currentPinList : LiveData<MutableList<PostEditPinVO>>

    fun postEditItemClick()
}