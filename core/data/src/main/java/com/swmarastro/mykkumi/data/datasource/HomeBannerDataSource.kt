package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.HomeBannerListDTO
import retrofit2.http.GET

interface HomeBannerDataSource {
    @GET("api/v1/banners")
    suspend fun getHomeBanners(
    ) : HomeBannerListDTO
}