package com.swmarastro.mykkumi.domain.usecase.post

import com.swmarastro.mykkumi.domain.entity.HobbyCategoryVO
import com.swmarastro.mykkumi.domain.repository.HobbyCategoryRepository
import javax.inject.Inject

class GetHobbyCategoryListUseCase @Inject constructor(
    private val repository: HobbyCategoryRepository,
) {
    suspend operator fun invoke(): HobbyCategoryVO {
        return repository.getHobbyCategoryList()
    }
}