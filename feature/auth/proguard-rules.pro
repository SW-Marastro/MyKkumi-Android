# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 공통 모듈의 클래스 유지
-keep class com.swmarastro.mykkumi.common.** { *; }

# 기타 ProGuard 설정
-keep class !a.a, * { *; }
-dontwarn a.a

-dontwarn com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn com.swmarastro.mykkumi.common_ui.custom.CustomScrollView
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemSkeletonPostListBinding
-dontwarn com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
-dontwarn com.swmarastro.mykkumi.data.datasource.HobbyCategoryDataSource
-dontwarn com.swmarastro.mykkumi.data.datasource.KakaoLoginDataSource
-dontwarn com.swmarastro.mykkumi.data.datasource.PreSignedUrlDataSource
-dontwarn com.swmarastro.mykkumi.data.datasource.PutImageS3DataSource
-dontwarn com.swmarastro.mykkumi.data.datasource.ReAccessTokenDataSource
-dontwarn com.swmarastro.mykkumi.data.datasource.UserInfoDataSource
-dontwarn com.swmarastro.mykkumi.data.di.DataStoreModule_ProvideAuthTokenDataStoreFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_PreSignedUrlDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideHobbyCategoryDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideKakaoInitializerFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideKakaoLoginDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideReAccessTokenDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideS3OkHttpClientFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideS3RetrofitFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideTokenInterceptorFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideUserInfoDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_PutImageS3DataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.interceptor.TokenAuthenticator
-dontwarn com.swmarastro.mykkumi.data.interceptor.TokenInterceptor
-dontwarn com.swmarastro.mykkumi.data.repository.HobbyCategoryRepositoryImpl
-dontwarn com.swmarastro.mykkumi.data.repository.KakaoLoginRepositoryImpl
-dontwarn com.swmarastro.mykkumi.data.repository.PreSignedUrlRepositoryImpl
-dontwarn com.swmarastro.mykkumi.data.repository.ReAccessTokenRepositoryImpl
-dontwarn com.swmarastro.mykkumi.data.repository.UserInfoRepositoryImpl
-dontwarn com.swmarastro.mykkumi.data.util.KakaoInitializer