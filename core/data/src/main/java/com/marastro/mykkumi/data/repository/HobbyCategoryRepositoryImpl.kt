package com.marastro.mykkumi.data.repository

import com.marastro.mykkumi.data.datasource.HobbyCategoryDataSource
import com.marastro.mykkumi.domain.entity.HobbyCategoryVO
import com.marastro.mykkumi.domain.repository.HobbyCategoryRepository
import javax.inject.Inject

class HobbyCategoryRepositoryImpl @Inject constructor(
    private val hobbyCategoryDataSource: HobbyCategoryDataSource,
) : HobbyCategoryRepository {

    override suspend fun getHobbyCategoryList(): HobbyCategoryVO {
        return hobbyCategoryDataSource.getHobbyCategoryList().toEntity()
    }
}