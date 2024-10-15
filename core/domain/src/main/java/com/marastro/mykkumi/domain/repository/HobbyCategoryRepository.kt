package com.marastro.mykkumi.domain.repository

import com.marastro.mykkumi.domain.entity.HobbyCategoryVO

interface HobbyCategoryRepository {
    suspend fun getHobbyCategoryList(): HobbyCategoryVO
}