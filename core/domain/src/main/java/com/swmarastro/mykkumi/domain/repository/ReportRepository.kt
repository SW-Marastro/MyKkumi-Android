package com.swmarastro.mykkumi.domain.repository

import com.swmarastro.mykkumi.domain.entity.ReportVO

interface ReportRepository {
    suspend fun reportPost(postId: Long): ReportVO
    suspend fun reportUser(userUuid: String): ReportVO
}