package com.swmarastro.mykkumi.domain.usecase.report

import com.swmarastro.mykkumi.domain.entity.ReportVO
import com.swmarastro.mykkumi.domain.repository.ReportRepository
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(postId: Long): ReportVO {
        return repository.reportPost(postId)
    }
}