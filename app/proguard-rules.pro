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
-keep class com.swmarastro.mykkumi.common_ui.** { *; }
-keep class com.swmarastro.mykkumi.data.** { *; }
-keep class java.lang.invoke.StringConcatFactory { *; }
-keep class com.swmarastro.mykkumi.common_ui.base.BaseFragment

# 카카오 로그인을 위한 카카오 SDK를 코드 축소, 난독화, 최적화에서 제외
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# https://github.com/square/okhttp/pull/6792
-keep class org.bouncycastle.jsse.**
-keep class org.conscrypt.*
-keep class org.openjsse.**

-dontobfuscate