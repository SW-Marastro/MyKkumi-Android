package com.marastro.mykkumi.data.datasource

import com.marastro.mykkumi.data.dto.response.BannerDetailDTO
import com.marastro.mykkumi.data.dto.response.BannerListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerDataSource {
    @GET("api/v1/banners")
    suspend fun getBannerList(
    ) : BannerListDTO //Response<HomeBannerListResponse>

    @GET("api/v1/banners/{id}")
    suspend fun getBannerDetail(
        @Path("id") id: Int
    ) : BannerDetailDTO// Response<HomeBannerItemDTO>
}