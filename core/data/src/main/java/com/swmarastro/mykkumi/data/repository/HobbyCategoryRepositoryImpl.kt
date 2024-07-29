package com.swmarastro.mykkumi.data.repository

import com.swmarastro.mykkumi.data.datasource.HobbyCategoryDataSource
import com.swmarastro.mykkumi.domain.entity.HobbyCategoryVO
import com.swmarastro.mykkumi.domain.repository.HobbyCategoryRepository
import javax.inject.Inject

class HobbyCategoryRepositoryImpl @Inject constructor(
    private val hobbyCategoryDataSource: HobbyCategoryDataSource,
) : HobbyCategoryRepository {

    override suspend fun getHobbyCategoryList(): HobbyCategoryVO {
        return hobbyCategoryDataSource.getHobbyCategoryList().toEntity()
    }
}