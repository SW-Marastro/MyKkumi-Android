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
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImagesAdapter
-dontwarn com.swmarastro.mykkumi.common_ui.base.BaseFragment
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemListLoadingBinding
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemPostWriterBinding
-dontwarn com.swmarastro.mykkumi.common_ui.post.PostImageIndicator

# 공통 모듈의 클래스 유지
-keep class com.swmarastro.mykkumi.common.** { *; }

# 기타 ProGuard 설정
-keep class !a.a, * { *; }
-dontwarn a.a
