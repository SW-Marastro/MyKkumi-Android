package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.HomeBannerItemDTO
import com.swmarastro.mykkumi.data.dto.response.HomeBannerListDTO
import com.swmarastro.mykkumi.domain.entity.HomeBannerListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeBannerDataSource {
    @GET("api/v1/banners")
    suspend fun getHomeBanners(
    ) : HomeBannerListDTO //Response<HomeBannerListResponse>

    @GET("api/v1/banners/{id}")
    suspend fun getHomeBannerDetail(
        @Path("id") id: Int
    ) : HomeBannerItemDTO// Response<HomeBannerItemDTO>
}