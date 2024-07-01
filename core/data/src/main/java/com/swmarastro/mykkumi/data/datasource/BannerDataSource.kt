package com.swmarastro.mykkumi.data.datasource

import com.swmarastro.mykkumi.data.dto.response.BannerItemDTO
import com.swmarastro.mykkumi.data.dto.response.BannerListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerDataSource {
    @GET("api/v1/banners")
    suspend fun getBannerList(
    ) : BannerListDTO //Response<HomeBannerListResponse>

    @GET("api/v1/banners/{id}")
    suspend fun getBannerDetail(
        @Path("id") id: Int
    ) : BannerItemDTO// Response<HomeBannerItemDTO>
}