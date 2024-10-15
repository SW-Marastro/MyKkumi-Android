package com.marastro.mykkumi.data.repository

import retrofit2.HttpException
import com.google.gson.Gson
import com.marastro.mykkumi.data.datasource.ReportDataSource
import com.marastro.mykkumi.data.dto.request.ReportPostRequestDTO
import com.marastro.mykkumi.data.dto.request.ReportUserRequestDTO
import com.marastro.mykkumi.domain.entity.ReportVO
import com.marastro.mykkumi.domain.exception.ApiException
import com.marastro.mykkumi.domain.exception.ErrorResponse
import com.marastro.mykkumi.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportDataSource: ReportDataSource,
) : ReportRepository {

    private companion object {
        private const val DUPLICATE_REPORT = "DUPLICATE_REPORT"
        private const val NOT_FOUND = "NOT_FOUND"
    }

    override suspend fun reportPost(postId: Long): ReportVO {
        return try {
            reportDataSource.reportPost(
                ReportPostRequestDTO(
                    postId = postId
                )
            ).toEntity()
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    override suspend fun reportUser(userUuid: String): ReportVO {
        return try {
            reportDataSource.reportUser(
                ReportUserRequestDTO(
                    userUuid = userUuid
                )
            ).toEntity()
        } catch (e: HttpException) {
            handleApiException(e)
        }
    }

    private fun handleApiException(exception: HttpException): Nothing {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

        when (errorResponse.errorCode) {
            DUPLICATE_REPORT -> throw ApiException.DuplicateReportException(errorResponse.message)
            NOT_FOUND -> throw ApiException.NotFoundException(errorResponse.message)
            else -> throw ApiException.UnknownApiException("An unknown error occurred: ${errorResponse.message}")
        }
    }
}