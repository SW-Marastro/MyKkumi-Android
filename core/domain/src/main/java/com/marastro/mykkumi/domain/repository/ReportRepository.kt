package com.marastro.mykkumi.domain.repository

import com.marastro.mykkumi.domain.entity.ReportVO

interface ReportRepository {
    suspend fun reportPost(postId: Long): ReportVO
    suspend fun reportUser(userUuid: String): ReportVO
}