package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HelloWorldVO

data class HelloWorldDTO(
    @SerializedName("title")
    val title: String
) {
    fun toEntity(): HelloWorldVO = HelloWorldVO(
        title = title
    )
}
