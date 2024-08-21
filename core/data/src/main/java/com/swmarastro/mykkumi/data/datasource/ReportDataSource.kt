package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.request.ReportPostRequestDTO
import com.swmarastro.mykkumi.data.dto.request.ReportUserRequestDTO
import com.swmarastro.mykkumi.data.dto.response.ReportResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportDataSource {
    @POST("/api/v1/report/post")
    suspend fun reportPost(
        @Body params: ReportPostRequestDTO
    ) : ReportResponseDTO

    @POST("/api/v1/report/user")
    suspend fun reportUser(
        @Body params: ReportUserRequestDTO
    ) : ReportResponseDTO
}