package com.marastro.mykkumi.domain.usecase.report

import com.marastro.mykkumi.domain.entity.ReportVO
import com.marastro.mykkumi.domain.repository.ReportRepository
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(postId: Long): ReportVO {
        return repository.reportPost(postId)
    }
}