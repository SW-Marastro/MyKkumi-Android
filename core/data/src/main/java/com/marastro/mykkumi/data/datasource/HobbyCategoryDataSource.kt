package com.marastro.mykkumi.data.datasource

import com.marastro.mykkumi.data.dto.response.HobbyCategoryDTO
import retrofit2.http.GET

interface HobbyCategoryDataSource {
    @GET("/api/v1/categories")
    suspend fun getHobbyCategoryList (
    ) : HobbyCategoryDTO
}