package com.marastro.mykkumi.domain.usecase.post

import com.marastro.mykkumi.domain.entity.HobbyCategoryVO
import com.marastro.mykkumi.domain.repository.HobbyCategoryRepository
import javax.inject.Inject

class GetHobbyCategoryListUseCase @Inject constructor(
    private val repository: HobbyCategoryRepository,
) {
    suspend operator fun invoke(): HobbyCategoryVO {
        return repository.getHobbyCategoryList()
    }
}