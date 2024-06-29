package com.swmarastro.mykkumi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.swmarastro.mykkumi.data.BuildConfig
import com.swmarastro.mykkumi.data.datasource.BannerDataSource

/*
@Module: 인터페이스나, 빌더 패턴을 사용한 경우, 외부 라이브러리 클래스 등등 생성자를 사용할 수 없는 Class를 주입해야 할 경우
@InstallIn: 어떤 Component에 Install할지 모듈 범위 지정. @Module이나 @EntryPoint 어노테이션과 함께 사용해야 함.
(SingletonComponent::class): 애플리케이션 전체에서 재사용 -> 네트워크 모듈은 전체에서 사용될듯하다
@Provides: 클래스가 외부 라이브러리를 사용(Retrofit, OkHttpClient, Room databases...)하거나 빌더 패턴으로 객체 생성을 하는 경우 @Provides로 의존성 생성
*/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // API BASE URL
    private const val BASE_URL = BuildConfig.BASE_URL

    // 로깅인터셉터 세팅
    @Provides
    @Singleton
    fun provideRequestHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    // OKHttpClient에 로깅인터셉터 등록
    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBannerDataSource(retrofit: Retrofit): BannerDataSource {
        return retrofit.create(BannerDataSource::class.java)
    }
}