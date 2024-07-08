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
#-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemListLoadingBinding
#-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemPostWriterBinding
#-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImageIndicator

# 공통 모듈의 클래스 유지
-keep class com.swmarastro.mykkumi.common.** { *; }

# 기타 ProGuard 설정
-keep class !a.a, * { *; }
-dontwarn a.a

#-dontwarn com.swmarastro.mykkumi.common_ui.base.BaseFragment
#-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemListLoadingBinding
#-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemPostWriterBinding
#-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImageIndicator
#-dontwarn com.swmarastro.mykkumi.data.datasource.BannerDataSource
#-dontwarn com.swmarastro.mykkumi.data.datasource.PostDataSource
#-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideBannerDataSourceFactory
#-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideOkHttpClientFactory
#-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvidePostDataSourceFactory
#-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideRequestHttpLoggingInterceptorFactory
#-dontwarn com.swmarastro.mykkumi.data.di.NetworkModule_ProvideRetrofitFactory
#-dontwarn com.swmarastro.mykkumi.data.repository.BannerRepositoryImpl
#-dontwarn com.swmarastro.mykkumi.data.repository.PostRepositoryImpl

-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImagesAdapter
-dontwarn com.swmarastro.mykkumi.common_ui.base.BaseFragment
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemListLoadingBinding
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemPostWriterBinding
-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImageIndicator