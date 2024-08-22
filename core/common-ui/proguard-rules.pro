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
-dontwarn com.swmarastro.mykkumi.common_ui.databinding.ItemSkeletonPostListBinding
-dontwarn com.swmarastro.mykkumi.common_ui.report.PostReportConfirmDialog
-dontwarn com.swmarastro.mykkumi.common_ui.server_driven.SpannableStringBuilderProvider$Companion
-dontwarn com.swmarastro.mykkumi.common_ui.server_driven.SpannableStringBuilderProvider
-dontwarn com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn com.swmarastro.mykkumi.common_ui.base.BaseBottomSheetFragment
-dontwarn com.swmarastro.mykkumi.common_ui.report.PostWriterReportConfirmDialog