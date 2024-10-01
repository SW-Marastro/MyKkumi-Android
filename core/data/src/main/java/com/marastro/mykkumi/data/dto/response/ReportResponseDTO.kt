package com.marastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.marastro.mykkumi.domain.entity.ReportVO

data class ReportResponseDTO(
    @SerializedName("result")
    val result: String
) {
    fun toEntity(): ReportVO = ReportVO(
        result = result
    )
}
