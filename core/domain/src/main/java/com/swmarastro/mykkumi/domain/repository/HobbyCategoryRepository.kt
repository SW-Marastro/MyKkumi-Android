package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.HobbyCategoryVO

interface HobbyCategoryRepository {
    suspend fun getHobbyCategoryList(): HobbyCategoryVO
}