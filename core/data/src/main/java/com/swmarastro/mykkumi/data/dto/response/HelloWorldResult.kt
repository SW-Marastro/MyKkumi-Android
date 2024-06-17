package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HelloWorld

data class HelloWorldResult(
    @SerializedName("title")
    val title: String
) {
    fun toEntity(): HelloWorld = HelloWorld(
        title = title
    )
}
