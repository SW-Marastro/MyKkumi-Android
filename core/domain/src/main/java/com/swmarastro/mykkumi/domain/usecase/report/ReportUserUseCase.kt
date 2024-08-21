package com.swmarastro.mykkumi.domain.usecase.report

import com.swmarastro.mykkumi.domain.entity.ReportVO
import com.swmarastro.mykkumi.domain.repository.ReportRepository
import javax.inject.Inject

class ReportUserUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(userUuid: String): ReportVO {
        return repository.reportUser(userUuid)
    }
}