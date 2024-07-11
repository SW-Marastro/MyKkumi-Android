package com.swmarastro.mykkumi.data.util

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.swmarastro.mykkumi.data.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class KakaoInitializer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var KAKAO_NATIVE_APP_KEY = BuildConfig.KAKAO_NATIVE_APP_KEY

    fun initialize() {
        KakaoSdk.init(context, KAKAO_NATIVE_APP_KEY)
    }
}