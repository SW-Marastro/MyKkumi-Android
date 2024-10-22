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
-keep class com.marastro.mykkumi.analytics.** { *; }
#-keep class java.lang.invoke.StringConcatFactory { *; }
-dontwarn java.lang.invoke.StringConcatFactory

# 카카오 로그인을 위한 카카오 SDK를 코드 축소, 난독화, 최적화에서 제외
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-dontwarn com.marastro.mykkumi.analytics.AnalyticsHelper
-dontwarn com.marastro.mykkumi.analytics.AnalyticsModule
-dontwarn com.marastro.mykkumi.analytics.AnalyticsModule_ProvideAnalyticsHelperFactory
-dontwarn com.marastro.mykkumi.analytics.AnalyticsModule_ProvideFirebaseAnalyticsFactory
-dontwarn hilt_aggregated_deps._com_marastro_mykkumi_analytics_AnalyticsModule