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
-dontwarn com.swmarastro.mykkumi.common_ui.base.BaseFragment
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemListLoadingBinding
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemPostWriterBinding
-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImageIndicator
-dontwarn com.swmarastro.mykkumi.data.datasource.BannerDataSource
-dontwarn com.swmarastro.mykkumi.data.datasource.PostDataSource
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideBannerDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideOkHttpClientFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvidePostDataSourceFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideRequestHttpLoggingInterceptorFactory
-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideRetrofitFactory
-dontwarn com.swmarastro.mykkumi.data.repository.BannerRepositoryImpl
-dontwarn com.swmarastro.mykkumi.data.repository.PostRepositoryImpl


# 카카오 로그인을 위한 카카오 SDK를 코드 축소, 난독화, 최적화에서 제외
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# https://github.com/square/okhttp/pull/6792
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**