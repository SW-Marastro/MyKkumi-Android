package com.swmarastro.mykkumi.data.di

import android.content.Context
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
import com.swmarastro.mykkumi.data.datasource.HobbyCategoryDataSource
import com.swmarastro.mykkumi.data.datasource.KakaoLoginDataSource
import com.swmarastro.mykkumi.data.datasource.PostDataSource
import com.swmarastro.mykkumi.data.datasource.PreSignedUrlDataSource
import com.swmarastro.mykkumi.data.datasource.PutImageS3DataSource
import com.swmarastro.mykkumi.data.datasource.ReAccessTokenDataSource
import com.swmarastro.mykkumi.data.datasource.ReportDataSource
import com.swmarastro.mykkumi.data.datasource.UserInfoDataSource
import com.swmarastro.mykkumi.data.interceptor.TokenAuthenticator
import com.swmarastro.mykkumi.data.interceptor.TokenInterceptor
import com.swmarastro.mykkumi.data.util.KakaoInitializer
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.domain.repository.ReAccessTokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Provider

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
    private var BASE_URL = BuildConfig.BASE_URL

    // 로깅인터셉터 세팅
    @Provides
    @Singleton
    fun provideRequestHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    // Token Interceptor - dependency cycle 방지
    @Provides
    fun provideTokenInterceptor(
        authTokenDataSource: AuthTokenDataStore,
        reAccessTokenRepository: Provider<ReAccessTokenRepository>,
    ) : TokenAuthenticator {
        return TokenAuthenticator(authTokenDataSource, reAccessTokenRepository)
    }

    // OKHttpClient에 로깅인터셉터 등록
    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenInterceptor) // header에 accessToken 넣어줌
            .authenticator(tokenAuthenticator) // token 만료 처리
            .build()
    }

    @Provides
    @Singleton
    @Named("S3Retrofit")
    fun provideS3OkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
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
    @Named("S3Retrofit")
    fun provideS3Retrofit(@Named("S3Retrofit") okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummy.base.url/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 카카오 sdk init을 위한
    @Provides
    @Singleton
    fun provideKakaoInitializer(@ApplicationContext context: Context) : KakaoInitializer {
        return KakaoInitializer(context)
    }

    @Provides
    @Singleton
    fun provideBannerDataSource(retrofit: Retrofit): BannerDataSource {
        return retrofit.create(BannerDataSource::class.java)
    }

    @Provides
    @Singleton
    fun providePostDataSource(retrofit: Retrofit): PostDataSource {
        return retrofit.create(PostDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoLoginDataSource(retrofit: Retrofit): KakaoLoginDataSource {
        return retrofit.create(KakaoLoginDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideUserInfoDataSource(retrofit: Retrofit): UserInfoDataSource {
        return retrofit.create(UserInfoDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideReAccessTokenDataSource(retrofit: Retrofit): ReAccessTokenDataSource {
        return retrofit.create(ReAccessTokenDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideHobbyCategoryDataSource(retrofit: Retrofit): HobbyCategoryDataSource {
        return retrofit.create(HobbyCategoryDataSource::class.java)
    }

    @Provides
    @Singleton
    fun preSignedUrlDataSource(retrofit: Retrofit): PreSignedUrlDataSource {
        return retrofit.create(PreSignedUrlDataSource::class.java)
    }

    @Provides
    @Singleton
    fun putImageS3DataSource(@Named("S3Retrofit") retrofit: Retrofit): PutImageS3DataSource {
        return retrofit.create(PutImageS3DataSource::class.java)
    }

    @Provides
    @Singleton
    fun reportDataSource(retrofit: Retrofit): ReportDataSource {
        return retrofit.create(ReportDataSource::class.java)
    }
}